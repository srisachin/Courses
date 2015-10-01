/***********************************************************/
/* (c) December 2002                                       */
/* College of Computing                                    */
/* Georgia Institute of Technolgy                          */
/* Project for Sven Koenig                                 */
/* "Comparing Lifelong Planning A* and A* in Simple Mazes" */
/***********************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <unistd.h>
#include <assert.h>

#if 1
// #define DEBUG
// #define LPASEARCHFROMSCRATCH
// #define GOALCANBEBLOCKED
// #define EIGHTCONNECTEDMAZE
// #define WEAKHEURISTICS
#define INFORMEDSEARCH
#define FASTMAZECHANGES
#define MAZEWIDTH 61
#define MAZEHEIGHT 61
#define MAZEDENSITY 0.3
#define MAZECHANGE 0.001
#define STARTX 50
#define STARTY 50
#define GOALX 10
#define GOALY 10
#define RUNS 100
#define REPLANNINGEPISODES 500
#define STATTIMES 5
#endif

#if 0
// #define DEBUG
// #define LPASEARCHFROMSCRATCH
// #define GOALCANBEBLOCKED
#define EIGHTCONNECTEDMAZE
#define INFORMEDSEARCH
#define FASTMAZECHANGES
#define MAZEWIDTH 40
#define MAZEHEIGHT 40
#define MAZEDENSITY 0.4
#define MAZECHANGE 0.005
#define STARTX 34
#define STARTY 34
#define GOALX 5
#define GOALY 5
#define RUNS 100
#define REPLANNINGEPISODES 500
#define STATTIMES 5
#endif

#if 0
// #define DEBUG
// #define LPASEARCHFROMSCRATCH
// #define GOALCANBEBLOCKED
#define EIGHTCONNECTEDMAZE
#define INFORMEDSEARCH
#define FASTMAZECHANGES
#define MAZEWIDTH 40
#define MAZEHEIGHT 40
#define MAZEDENSITY 0.4
#define MAZECHANGE 0.005
#define STARTX 34
#define STARTY 20
#define GOALX 5
#define GOALY 20
#define RUNS 100
#define REPLANNINGEPISODES 500
#define STATTIMES 5
#endif

#define LARGE 499
#define BASE 500
#define max(x, y) ((x) > (y) ? (x) : (y))
#define min(x, y) ((x) < (y) ? (x) : (y))

#ifdef EIGHTCONNECTEDMAZE
#define DIRECTIONS 8
int dx[DIRECTIONS] = {1, 0, -1, 0, 1, 1, -1, -1};
int dy[DIRECTIONS] = {0, 1, 0, -1, 1, -1, 1, -1};
int reverse[DIRECTIONS] = {2, 3, 0, 1, 7, 6, 5, 4};
#else
#define DIRECTIONS 4 
int dx[DIRECTIONS] = {1, 0, -1,  0};
int dy[DIRECTIONS] = {0, 1,  0, -1};
int reverse[DIRECTIONS] = {2, 3, 0, 1};
#endif

int statexpanded;
int statpercolated;
int statruns;
int statnopath;
float stattimes[STATTIMES];

struct cell;
typedef struct cell cell;

struct cell
{
#ifdef FASTMAZECHANGES
  int x, y;
#endif
  cell *move[DIRECTIONS];
  cell *searchtree;
  short obstacle;
  int g;
  int rhs;
  int h;
  int key;
  int heapindex;
  int iteration;
};

cell **maze = NULL;
cell *mazestart, *mazegoal;
int mazeiteration = 0;

#ifdef FASTMAZECHANGES
cell **changes[2];
int changescounter[2];
#endif

#ifdef DEBUG
#define TESTARRAYSIZE 100000
int testarray[TESTARRAYSIZE]; 
int testcounter; 
int testfirst;
#endif

/* ---------------------------------------------------------------------- */

#define HEAPSIZE 100000
cell *heap[HEAPSIZE];
int heapsize = 0;

void percolatedown(int hole, cell *tmp)
{
  int child;

  if (heapsize != 0)
    {
      for (; 2*hole <= heapsize; hole = child)
	{
	  child = 2*hole;
	  if (child != heapsize && heap[child+1]->key < heap[child]->key)
	    ++child;
	  if (heap[child]->key < tmp->key)
	    {
	      heap[hole] = heap[child];
	      heap[hole]->heapindex = hole;
	      ++statpercolated;
	    }
	  else
	    break;
	}
      heap[hole] = tmp;
      heap[hole]->heapindex = hole;
    }
}

void percolateup(int hole, cell *tmp)
{
  if (heapsize != 0)
    {
      for (; hole > 1 && tmp->key < heap[hole/2]->key; hole /= 2)
	{
	  heap[hole] = heap[hole/2];
	  heap[hole]->heapindex = hole;
	  ++statpercolated;
	}  
      heap[hole] = tmp;
      heap[hole]->heapindex = hole;
    }
}

void percolateupordown(int hole, cell *tmp)
{
  if (heapsize != 0)
    {
      if (hole > 1 && heap[hole/2]->key > tmp->key)
	percolateup(hole, tmp);
      else
	percolatedown(hole, tmp);
    }
}

void insertheap(cell *thiscell)
{
  int hole;

  if (thiscell->heapindex == 0)
    {
      assert(heapsize < HEAPSIZE-1);
      percolateup(++heapsize, thiscell);
    }
  else
    percolateupordown(thiscell->heapindex, heap[thiscell->heapindex]);
}

void deleteheap(cell *thiscell)
{
  if (thiscell->heapindex != 0)
    {
      percolateupordown(thiscell->heapindex, heap[heapsize--]);
      thiscell->heapindex = 0;
    }
}

cell *topheap()
{
  if (heapsize == 0)
    return NULL;
  return heap[1];
}

void emptyheap()
{
  int i;

  for (i = 1; i <= heapsize; ++i)
    heap[i]->heapindex = 0;
  heapsize = 0;
}

/* ---------------------------------------------------------------------- */

void printmaze()
{
  int x, y;

  printf("width = %d, height = %d, steps = 1\n", MAZEWIDTH, MAZEHEIGHT);
  printf("start(x,y) = (%d,%d), goal(x,y) = (%d,%d)\n", STARTX, STARTY, GOALX, GOALY);
  for (y = 0; y < MAZEHEIGHT; ++y)
    {
      for (x = 0; x < MAZEWIDTH; ++x)
	printf("%1d ", maze[y][x].obstacle);
      printf("\n");
    }
  printf("\n\n");
}

void newrandommaze()
{
  int d;
  int x, y;
  int newx, newy;
  int goaly = GOALY, goalx = GOALX;
  int starty = STARTY, startx = STARTX;
 
  mazeiteration = 0;
#ifdef FASTMAZECHANGES
  changescounter[0] = changescounter[1] = 0;
#endif
  if (maze == NULL)
    {
      maze = (cell **)calloc(MAZEHEIGHT, sizeof(cell *));
#ifdef FASTMAZECHANGES
      changes[0] = (cell **)calloc(MAZEHEIGHT*MAZEWIDTH, sizeof(cell *));
      changes[1] = (cell **)calloc(MAZEHEIGHT*MAZEWIDTH, sizeof(cell *));
#endif
      for (y = 0; y < MAZEHEIGHT; ++y)
	{
	  maze[y] = (cell *)calloc(MAZEWIDTH, sizeof(cell));
	  for (x = 0; x < MAZEWIDTH; ++x)
	    {
#ifdef FASTMAZECHANGES
	      maze[y][x].y = y;
	      maze[y][x].x = x;
#endif
#ifdef INFORMEDSEARCH
#ifdef EIGHTCONNECTEDMAZE
	      maze[y][x].h = max(abs(y - goaly), abs(x - goalx));
#else
#ifdef WEAKHEURISTICS
	      maze[y][x].h = max(abs(y - goaly), abs(x - goalx));
#else
	      maze[y][x].h = abs(y - goaly) + abs(x - goalx);
#endif
#endif
#else
	      maze[y][x].h = 0;
#endif
	    }
	}
      mazestart = &maze[starty][startx];
      mazegoal = &maze[goaly][goalx];
    }
  for (y = 0; y < MAZEHEIGHT; ++y)
    for (x = 0; x < MAZEWIDTH; ++x)
      {
	maze[y][x].iteration = mazeiteration;
        maze[y][x].obstacle = (random() % 10000 < 10000 * MAZEDENSITY);
	// maze[y][x].obstacle = (random() <= RAND_MAX * MAZEDENSITY); does not work for some compilers
#ifdef GOALCANBEBLOCKED
	if (&maze[y][x] == mazestart)
#else
	if (&maze[y][x] == mazestart || &maze[y][x] == mazegoal)
#endif
	  maze[y][x].obstacle = 0;
#ifdef FASTMAZECHANGES
	else
	  changes[maze[y][x].obstacle][changescounter[maze[y][x].obstacle]++] = &maze[y][x];
#endif
	for (d = 0; d < DIRECTIONS; ++d)
	  maze[y][x].move[d] = NULL;
      }
  for (y = 0; y < MAZEHEIGHT; ++y)
    for (x = 0; x < MAZEWIDTH; ++x)
      if (!maze[y][x].obstacle)
	for (d = 0; d < DIRECTIONS; ++d)
	  {
	    newy = y + dy[d];
	    newx = x + dx[d];
	    if (newy >= 0 && newy < MAZEHEIGHT && newx >= 0 && newx < MAZEWIDTH && !maze[newy][newx].obstacle)
	      maze[y][x].move[d] = &maze[newy][newx];
	  }
}

/* ---------------------------------------------------------------------- */

void astar(int tiebreaking) /* -1 = breaking ties in favor of larger g-values; 1 = breaking ties in favor of smaller g-values */
{
  int x, y;
  cell *tmpcell;
  int d;
  struct timeval tv1, tv2;

  gettimeofday(&tv1, NULL);
  ++statruns;
  ++mazeiteration;
  mazestart->g = 0;
  mazestart->key = mazestart->h * BASE;
  mazestart->searchtree = NULL;
  mazestart->iteration = mazeiteration;
  emptyheap();
  insertheap(mazestart);
  while (topheap() != NULL)
    {
      tmpcell = topheap();
      if (tmpcell == mazegoal)
	break;
      deleteheap(tmpcell);
      ++statexpanded;
      for (d = 0; d < DIRECTIONS; ++d)
	if (tmpcell->move[d] != NULL && (tmpcell->move[d]->iteration != mazeiteration || tmpcell->g + 1 < tmpcell->move[d]->g)) 
	  {
	    tmpcell->move[d]->iteration = mazeiteration;
	    tmpcell->move[d]->g = tmpcell->g + 1;
	    tmpcell->move[d]->key = (tmpcell->move[d]->g + tmpcell->move[d]->h) * BASE + tiebreaking * tmpcell->move[d]->g;
	    tmpcell->move[d]->searchtree = tmpcell;
	    insertheap(tmpcell->move[d]);
	  }
    }
  gettimeofday(&tv2, NULL);
  stattimes[0] += 1.0*(tv2.tv_sec - tv1.tv_sec) + 1.0*(tv2.tv_usec - tv1.tv_usec)/1000000.0;
  statnopath += (topheap() == NULL);
#ifdef DEBUG
  d = LARGE;
  if (topheap() != NULL)
    { /* tracing back a shortest path from mazestart to mazegoal */
      tmpcell = mazegoal;
      for (d = 0; tmpcell != mazestart; ++d, tmpcell = tmpcell->searchtree) {}
      assert(d == mazegoal->g);
    }
  assert(testcounter < TESTARRAYSIZE);
  if (testfirst)
    testarray[testcounter++] = d;
  else if (testarray[testcounter++] != d)
    printf("Error: the length of the path found is different from before!\n");
#endif
}

void testastar(int tiebreaking) /* -1 = breaking ties in favor of larger g-values; 1 = breaking ties in favor of smaller g-values */
{
  int x, y;
  int newx, newy;
  int d;
  int i, j, k;
  struct timeval tv1, tv2, tv3;

  assert(tiebreaking == 1 || tiebreaking == -1);
#ifdef DEBUG
  testcounter = 0;
#endif
  gettimeofday(&tv1, NULL);
  statexpanded = 0;
  statpercolated = 0;
  statruns = 0;
  statnopath = 0;
  for (d=0; d < STATTIMES; ++d)
    stattimes[d] = 0.0;
  for (k = 0; k < RUNS; ++k)
    {
      newrandommaze();
      // printmaze();
      gettimeofday(&tv2, NULL);
      astar(tiebreaking);
      for (i = 0; i < REPLANNINGEPISODES; ++i)
	{
	  gettimeofday(&tv3, NULL);
	  if (i+1 < STATTIMES)
	    stattimes[i+1] += 1.0*(tv3.tv_sec - tv2.tv_sec) + 1.0*(tv3.tv_usec - tv2.tv_usec)/1000000.0;
	  for (j = 0; j < MAZECHANGE*MAZEWIDTH*MAZEHEIGHT-0.01; ++j)
	    {
#ifdef FASTMAZECHANGES
	      d = random() % changescounter[1];
	      x = changes[1][d]->x;
	      y = changes[1][d]->y;
	      changes[0][changescounter[0]++] = changes[1][d];
	      changes[1][d] = changes[1][--changescounter[1]];
#else
	      for (;;)
		{
		  y = random() % MAZEHEIGHT;
		  x = random() % MAZEWIDTH;
		  if (maze[y][x].obstacle)
		    break;
		}
#endif
	      maze[y][x].obstacle = 0;
	      for (d = 0; d < DIRECTIONS; ++d)
		{
		  newy = y + dy[d];
		  newx = x + dx[d];
		  if (newy >= 0 && newy < MAZEHEIGHT && newx >= 0 && newx < MAZEWIDTH && !maze[newy][newx].obstacle)
		    {
		      maze[y][x].move[d] = &maze[newy][newx];
		      maze[newy][newx].move[reverse[d]] = &maze[y][x];
		    }
		}
	    }
	  for (j = 0; j < MAZECHANGE*MAZEWIDTH*MAZEHEIGHT-0.01; ++j)
	    {
#ifdef FASTMAZECHANGES
	      d = random() % changescounter[0];
	      x = changes[0][d]->x;
	      y = changes[0][d]->y;
	      changes[1][changescounter[1]++] = changes[0][d];
	      changes[0][d] = changes[0][--changescounter[0]];
#else
	      for (;;)
		{
		  y = random() % MAZEHEIGHT;
		  x = random() % MAZEWIDTH;
#ifdef GOALCANBEBLOCKED
		  if (!maze[y][x].obstacle && &maze[y][x] != mazestart)
#else
		  if (!maze[y][x].obstacle && &maze[y][x] != mazestart && &maze[y][x] != mazegoal) 
#endif
		    break;
		}
#endif
	      maze[y][x].obstacle = 1;
	      for (d = 0; d < DIRECTIONS; ++d)
		{
		  newy = y + dy[d];
		  newx = x + dx[d];
		  if (newy >= 0 && newy < MAZEHEIGHT && newx >= 0 && newx < MAZEWIDTH && !maze[newy][newx].obstacle) 
		    {
		      maze[y][x].move[d] = NULL;
		      maze[newy][newx].move[reverse[d]] = NULL;
		    }
		}
	    }
	  astar(tiebreaking);
	}
    }
  gettimeofday(&tv3, NULL);
  printf("A*:   cell expansions/replanning episode = %7.2f, \n \
          heap percolates/replanning episode = %7.2f,       \n \
          no path found/replanning episode = %5.2f percent, \n \
          total time in main search routine/run = %7.4f,    \n \
          total time/run = %7.4f\n",
	 1.0*statexpanded/statruns, 1.0*statpercolated/statruns, 100.0*statnopath/statruns, 1000.0*stattimes[0]/RUNS,
 	 1000.0*(1.0*(tv3.tv_sec - tv1.tv_sec) + 1.0*(tv3.tv_usec - tv1.tv_usec)/1000000.0)/RUNS);
  for (d = 1; d < STATTIMES; ++d)
    printf("time[%d] = %7.4f ", d, 1000.0*stattimes[d]/RUNS);
  printf("\n");
#ifdef DEBUG
  testfirst = 0;
#endif
}

/* ---------------------------------------------------------------------- */

void lpastarinitialize()
{
  ++mazeiteration;
  emptyheap();
  mazestart->g = LARGE;
  mazestart->rhs = 0;
  mazestart->key = mazestart->h * BASE;
  mazestart->searchtree = NULL;
  mazestart->iteration = mazeiteration;
  insertheap(mazestart);
  mazegoal->g = LARGE;
  mazegoal->rhs = LARGE;
  mazegoal->searchtree = NULL;
  mazegoal->iteration = mazeiteration;
}

void initializecell(cell *thiscell)
{
  if (thiscell->iteration != mazeiteration)
    {
      thiscell->g = LARGE;
      thiscell->rhs = LARGE;
      thiscell->searchtree = NULL;
      thiscell->iteration = mazeiteration;
    }
}

void lpastarupdatecell(cell *thiscell)
{
  if (thiscell->g != thiscell->rhs)
    {
      thiscell->key = (min(thiscell->g, thiscell->rhs) + thiscell->h) * BASE + min(thiscell->g, thiscell->rhs);
      insertheap(thiscell);
    }
  else 
    deleteheap(thiscell);
}

void lpastarupdaterhs(cell *thiscell)
{
  int d;
  cell *tmpcell;

  thiscell->rhs = LARGE;
  thiscell->searchtree = NULL;
  for (d = 0; d < DIRECTIONS; ++d)
    if (thiscell->move[d] != NULL && thiscell->move[d]->iteration == mazeiteration && thiscell->rhs > thiscell->move[d]->g + 1)
      {
	thiscell->rhs = thiscell->move[d]->g + 1; 
	thiscell->searchtree = thiscell->move[d];
      }
  lpastarupdatecell(thiscell);
}

void lpastarcomputeshortestpath() // = this version assumes that a cell cannot be its own successor
{
  cell *tmpcell1, *tmpcell2;
  int d;
  struct timeval tv1, tv2;

  gettimeofday(&tv1, NULL);
  ++statruns;
  while (mazegoal->rhs > mazegoal->g || (topheap() != NULL && topheap()->key < 
         min(mazegoal->g, mazegoal->rhs) * BASE + min(mazegoal->g, mazegoal->rhs)))
    {
      tmpcell1 = topheap();
      ++statexpanded;
      if (tmpcell1->g > tmpcell1->rhs)
	{
	  tmpcell1->g = tmpcell1->rhs;
	  deleteheap(tmpcell1);
	  for (d = 0; d < DIRECTIONS; ++d)
	    if (tmpcell1->move[d] != NULL)
	      {
		tmpcell2 = tmpcell1->move[d];
		initializecell(tmpcell2);
		if (tmpcell2 != mazestart && tmpcell2->rhs > tmpcell1->g + 1)
		  {
		    tmpcell2->rhs = tmpcell1->g + 1;
		    tmpcell2->searchtree = tmpcell1;
		    lpastarupdatecell(tmpcell2);
		  }
	      }
	}
      else
	{
	  tmpcell1->g = LARGE;
	  lpastarupdatecell(tmpcell1);
	  for (d = 0; d < DIRECTIONS; ++d) 
	    if (tmpcell1->move[d] != NULL)
	      {
		tmpcell2 = tmpcell1->move[d];
                initializecell(tmpcell2);
		if (tmpcell2 != mazestart && tmpcell2->searchtree == tmpcell1)
		  lpastarupdaterhs(tmpcell2);
	      }
	}
    }
  gettimeofday(&tv2, NULL);
  stattimes[0] += 1.0*(tv2.tv_sec - tv1.tv_sec) + 1.0*(tv2.tv_usec - tv1.tv_usec)/1000000.0;
  statnopath += (mazegoal->rhs == LARGE);
#ifdef DEBUG 
  d = LARGE;
  if (mazegoal->rhs != LARGE)
    { /* tracing back a shortest path from mazestart to mazegoal */
      tmpcell1 = mazegoal;
      for (d = 0; tmpcell1 != mazestart; ++d, tmpcell1 = tmpcell1->searchtree) {}
      assert(d == mazegoal->rhs);
    }
  if (testfirst)
    testarray[testcounter++] = d;
  else if (testarray[testcounter++] != d)
    printf("Error: the length of the path found is different from before!\n");
#endif
}

void testlpastar() // = this version is specific to mazes
{
  int x, y;
  int newx, newy;
  int d;
  int i, j, k;
  struct timeval tv1, tv2, tv3;

#ifdef DEBUG
  testcounter = 0;
#endif
  gettimeofday(&tv1, NULL);
  statexpanded = 0;
  statpercolated = 0;
  statruns = 0;
  statnopath = 0;
  for (d=0; d < STATTIMES; ++d)
    stattimes[d] = 0.0;
  for (k = 0; k < RUNS; ++k)
    {  
      newrandommaze();
      gettimeofday(&tv2, NULL);
      lpastarinitialize();
      lpastarcomputeshortestpath();
      for (i = 0; i < REPLANNINGEPISODES; ++i)
	{
	  gettimeofday(&tv3, NULL);
	  if (i+1 < STATTIMES)
	    stattimes[i+1] += 1.0*(tv3.tv_sec - tv2.tv_sec) + 1.0*(tv3.tv_usec - tv2.tv_usec)/1000000.0;
	  for (j = 0; j < MAZECHANGE*MAZEWIDTH*MAZEHEIGHT-0.01; ++j)
	    {
#ifdef FASTMAZECHANGES
              d = random() % changescounter[1];
              x = changes[1][d]->x;
              y = changes[1][d]->y;
              changes[0][changescounter[0]++] = changes[1][d];
              changes[1][d] = changes[1][--changescounter[1]];
#else
	      for (;;)
		{
		  y = random() % MAZEHEIGHT;
		  x = random() % MAZEWIDTH;
		  if (maze[y][x].obstacle)
		    break;
		}
#endif
	      maze[y][x].obstacle = 0;
	      initializecell(&maze[y][x]);
	      for (d = 0; d < DIRECTIONS; ++d)
		{
		  newy = y + dy[d];
		  newx = x + dx[d];
		  if (newy >= 0 && newy < MAZEHEIGHT && newx >= 0 && newx < MAZEWIDTH && !maze[newy][newx].obstacle)
		    {
		      maze[y][x].move[d] = &maze[newy][newx];
		      maze[newy][newx].move[reverse[d]] = &maze[y][x];
		      initializecell(&maze[newy][newx]);
		      if (&maze[newy][newx] != mazestart && maze[newy][newx].rhs > maze[y][x].g + 1)
			{
			  maze[newy][newx].rhs = maze[y][x].g + 1;
			  maze[newy][newx].searchtree = &maze[y][x];
			  lpastarupdatecell(&maze[newy][newx]);
			}
		    }
		}
	      if (&maze[y][x] != mazestart)
		lpastarupdaterhs(&maze[y][x]);
	    }
	  for (j = 0; j < MAZECHANGE*MAZEWIDTH*MAZEHEIGHT-0.01; ++j)
	    {
#ifdef FASTMAZECHANGES
              d = random() % changescounter[0];
              x = changes[0][d]->x;
              y = changes[0][d]->y;
              changes[1][changescounter[1]++] = changes[0][d];
              changes[0][d] = changes[0][--changescounter[0]];
#else
	      for (;;)
		{
		  y = random() % MAZEHEIGHT;
		  x = random() % MAZEWIDTH;
#ifdef GOALCANBEBLOCKED
		  if (!maze[y][x].obstacle && &maze[y][x] != mazestart)
#else
		  if (!maze[y][x].obstacle && &maze[y][x] != mazestart && &maze[y][x] != mazegoal) 
#endif
		    break;
		}
#endif
	      maze[y][x].obstacle = 1;
	      initializecell(&maze[y][x]);
	      for (d = 0; d < DIRECTIONS; ++d)
		{
		  newy = y + dy[d];
		  newx = x + dx[d];
		  if (newy >= 0 && newy < MAZEHEIGHT && newx >= 0 && newx < MAZEWIDTH && !maze[newy][newx].obstacle)
		    {
		      maze[y][x].move[d] = NULL;
		      maze[newy][newx].move[reverse[d]] = NULL;
		      initializecell(&maze[newy][newx]);
		      if (&maze[newy][newx] != mazestart && maze[newy][newx].searchtree == &maze[y][x])
			lpastarupdaterhs(&maze[newy][newx]);
		    }
		}
              if (&maze[y][x] != mazestart)
		{
		  maze[y][x].rhs = LARGE;
		  lpastarupdatecell(&maze[y][x]);
		}
	    }
#ifdef LPASEARCHFROMSCRATCH
	  lpastarinitialize(); 
#endif
	  lpastarcomputeshortestpath();
	}
    }
  gettimeofday(&tv3, NULL);
  printf("LPA*: cell expansions/replanning episode = %7.2f, \n \
          heap percolates/replanning episode = %7.2f,       \n \
          no path found/replanning episode = %5.2f percent, \n \
          total time in main search routine/run = %7.4f,    \n \
          total time/run = %7.4f\n",
	 1.0*statexpanded/statruns, 1.0*statpercolated/statruns, 100.0*statnopath/statruns, 1000.0*stattimes[0]/RUNS,
 	 1000.0*(1.0*(tv3.tv_sec - tv1.tv_sec) + 1.0*(tv3.tv_usec - tv1.tv_usec)/1000000.0)/RUNS);
  for (d = 1; d < STATTIMES; ++d)
    printf("time[%d] = %7.4f ", d, 1000.0*stattimes[d]/RUNS);
  printf("\n");
#ifdef DEBUG
  testfirst = 0;
#endif
}

/* ---------------------------------------------------------------------- */

int main(int argc, char *argv[])
{
  unsigned int seed;

  printf("width = %d, height = %d, obstacle density = %.2f, start= (%d,%d), goal= (%d,%d)\n",
	 MAZEWIDTH, MAZEHEIGHT, MAZEDENSITY, STARTX, STARTY, GOALX, GOALY);
#ifdef EIGHTCONNECTEDMAZE
  printf("The maze is eight-connected.\n");
#else
  printf("The maze is four-connected.\n");
#endif
#ifdef WEAKHEURISTICS
  printf("The heuristics is the maximum of the difference in x and y coordinates, rather than the Manhattan distance.\n");
#endif
  printf("runs = %d, replanning episodes/run = %d, cells changing per replanning episode = %d (%.2f percent)\n", 
	 RUNS, REPLANNINGEPISODES, 2*(int)(MAZECHANGE*MAZEWIDTH*MAZEHEIGHT), 200.0*(int)(MAZECHANGE*MAZEWIDTH*MAZEHEIGHT)/MAZEWIDTH/MAZEHEIGHT);
#ifdef GOALCANBEBLOCKED
  printf("The goal cell can get blocked.\n\n");
#else
  printf("The goal cell cannot get blocked.\n\n");
#endif
#ifdef DEBUG
  testfirst = 1;
#endif
  seed = (unsigned int)time(NULL);
  srandom(seed);
#ifdef LPASTARSEARCHFROMSCRATCH
  printf("LPA* searches each time from scratch.\n");
#else
  printf("LPA* replans.\n");
#endif
  testlpastar();
  srandom(seed);
  printf("A* breaks ties in favor of cells with larger g-values.\n");
  testastar(-1);
  srandom(seed);
  printf("A* breaks ties in favor of cells with smaller g-values.\n");
  testastar(1);
}
