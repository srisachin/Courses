import heapq
import random
import pygame

BLACK    = (   0,   0,   0)
WHITE    = ( 255, 255, 255)
GREEN    = (   0, 255,   0)
RED      = ( 255,   0,   0)
BLUE     = ( 0,   0,   255)
 
width  = 20
height = 20
 
margin = 5

size = [280, 280]

    
screen = pygame.display.set_mode(size)
 
class Grid(object):
    def __init__(self, x, y, reachable):
        """
        Initialize new grid

        @param x grid x coordinate
        @param y grid y coordinate
        @param reachable is grid reachable? not a wall?
        """
        self.reachable = reachable
        self.visited = False
        self.path = False
        self.x = x
        self.y = y
        self.parent = None
        self.g = 0
        self.h = 0
        self.f = 0
    def __lt__(self, other):
        return self.f < other.f
    
class AStar(object):
    def __init__(self):
        self.opened = []
        heapq.heapify(self.opened)
        self.closed = set()
        self.grids = []
        self.grid_height = 11
        self.grid_width = 11

    def init_grid(self):
        for x in range(self.grid_width):
            for y in range(self.grid_height):
                reachable = True
                if random.randint(1,10) < 4:
                    reachable = False
                """if x==1 and y==1:
                    reachable = True
                if x==self.grid_width-1 and y==self.grid_height-1:
                    reachable = True"""
                self.grids.append(Grid(x, y, reachable))
        self.start = self.get_grid(0, 0)
        self.end = self.get_grid(10, 10)
        self.start.reachable = True
        self.end.reachable = True
        self.start.path = True
        self.end.path = True

    def get_heuristic(self, grid):
        """
        Compute the heuristic value H for a grid: distance between
        this grid and the ending grid multiply by 10.

        @param grid
        @returns heuristic value H
        """
        return 10 * (abs(grid.x - self.end.x) + abs(grid.y - self.end.y))

    def get_grid(self, x, y):
        """
        Returns a grid from the grids list

        @param x grid x coordinate
        @param y grid y coordinate
        @returns grid
        """
        return self.grids[x * self.grid_height + y]

    def get_adjacent_grids(self, grid):
        """
        Returns adjacent grids to a grid. Clockwise starting
        from the one on the right.

        @param grid get adjacent grids for this grid
        @returns adjacent grids list 
        """
        grids = []
        if grid.x < self.grid_width-1:
            grids.append(self.get_grid(grid.x+1, grid.y))
        if grid.y > 0:
            grids.append(self.get_grid(grid.x, grid.y-1))
        if grid.x > 0:
            grids.append(self.get_grid(grid.x-1, grid.y))
        if grid.y < self.grid_height-1:
            grids.append(self.get_grid(grid.x, grid.y+1))
        return grids

    def display_path(self):
        grid = self.end
        while grid.parent is not self.start:
            grid = grid.parent
            grid.path = True
            print('path: grid: %d,%d' % (grid.x, grid.y))
        
        
    def display_grid(self):
        pygame.display.set_caption("A star implementation")
        screen.fill(BLACK)
        done = False
        while done == False:
            for event in pygame.event.get(): # User did something
                if event.type == pygame.QUIT: # If user clicked close
                    done = True
                #else:
                elif event.type == pygame.MOUSEBUTTONDOWN:
                    done = True
            # Draw the grid
                for grid in self.grids:
                        color = WHITE
                        if grid.reachable == True:
                            color = GREEN
                        if grid.path == True:
                            color = RED
                        pygame.draw.rect(screen, color,
                             [(margin+width)*grid.x+margin, (margin+height)*grid.y+margin,
                             #[(margin+width)*grid.x, (margin+height)*grid.y,
                              width, height])
                    
            pygame.display.flip()
       
            
    """def compare(self, grid1, grid2):
        ""
        Compare 2 grids F values

        @param grid1 1st grid
        @param grid2 2nd grid
        @returns -1, 0 or 1 if lower, equal or greater
        ""
        if grid1.f < grid2.f:
            return -1
        elif grid1.f > grid2.f:
            return 1
        return 0
    """
    def update_grid(self, adj, grid):
        """
        Update adjacent grid

        @param adj adjacent grid to current grid
        @param grid current grid being processed
        """
        adj.g = grid.g + 10
        adj.h = self.get_heuristic(adj)
        adj.parent = grid
        adj.f = adj.h + adj.g

    def process(self):        
        # add starting grid to open heap queue
        heapq.heappush(self.opened, (self.start.f, self.start))
        while len(self.opened):
            # pop grid from heap queue 
            f, grid = heapq.heappop(self.opened)
            # add grid to closed list so we don't process it twice
            self.closed.add(grid)
            # if ending grid, display found path
            if grid is self.end:
                self.display_path()
                break
            # get adjacent grids for grid
            adj_grids = self.get_adjacent_grids(grid)
            for adj_grid in adj_grids:
                if adj_grid.reachable and adj_grid not in self.closed:
                    if (adj_grid.f, adj_grid) in self.opened:
                        # if adj grid in open list, check if current path is
                        # better than the one previously found
                        # for this adj grid.
                        if adj_grid.g > grid.g + 10:
                            self.update_grid(adj_grid, grid)
                    else:
                        self.update_grid(adj_grid, grid)
                        # add adj grid to open list
                        heapq.heappush(self.opened, (adj_grid.f, adj_grid))


def main():
    pygame.init()
 

    # Set title of screen

    a = AStar()
    a.init_grid()
    a.display_grid()
    a.process()
    a.display_grid()
    pygame.quit()
    
if __name__ == '__main__':
    main()

"""
# Used to manage how fast the screen updates
clock = pygame.time.Clock()
 
# -------- Main Program Loop -----------
while done == False:
    for event in pygame.event.get(): # User did something
        if event.type == pygame.QUIT: # If user clicked close
            done = True # Flag that we are done so we exit this loop
        ""elif event.type == pygame.MOUSEBUTTONDOWN:
            # User clicks the mouse. Get the position
            pos = pygame.mouse.get_pos()
            # Change the x/y screen coordinates to grid coordinates
            column = pos[0] // (width + margin)
            row = pos[1] // (height + margin)
            # Set that location to zero
            grid[row][column] = 1
            print("Click ", pos, "Grid coordinates: ", row, column)""
 
    # Set the screen background
    
 
    # Limit to 60 frames per second
    clock.tick(60)
"""
