import heapq
import random
import pygame

BLACK    = (   0,   0,   0)
WHITE    = ( 255, 255, 255)
GREEN    = (   0, 255,   0)
RED      = ( 255,   0,   0)
BLUE     = ( 0,   0,   255)
 
width  = 5
height = 5
 
margin = 2

size = [710, 710]

    
screen = pygame.display.set_mode(size)
 
class Grid(object):
    def __init__(self, x, y, blocked):
        self.blocked = blocked
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
    
class Gridworld(object):
    def __init__(self):
        self.opened = []
        heapq.heapify(self.opened)
        self.closed = set()
        self.grids = []
        self.grid_height = 101
        self.grid_width = 101

    def init_grid(self):
        for x in range(self.grid_width):
            for y in range(self.grid_height):
                blocked = False
                if random.randint(1,10) < 4:
                    blocked = True
                self.grids.append(Grid(x, y, blocked))
        self.start = self.get_grid(0, 0)
        self.end = self.get_grid(100, 100)
        self.start.blocked = False
        self.end.blocked = False
        self.start.path = True
        self.end.path = True

    def get_heuristic(self, grid):
        return 10 * (abs(grid.x - self.end.x) + abs(grid.y - self.end.y))

    def get_grid(self, x, y):
        return self.grids[x * self.grid_height + y]

    def get_adjacent_grids(self, grid):
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
            for event in pygame.event.get(): 
                if event.type == pygame.QUIT: 
                    done = True
                #else:
                elif event.type == pygame.MOUSEBUTTONDOWN:
                    done = True
            # Draw the grid
                for grid in self.grids:
                        color = WHITE
                        if grid.blocked == False:
                            color = GREEN
                        if grid.path == True:
                            color = RED
                        pygame.draw.rect(screen, color,
                             [(margin+width)*grid.x+margin, (margin+height)*grid.y+margin,
                             #[(margin+width)*grid.x, (margin+height)*grid.y,
                              width, height])
                    
            pygame.display.flip()
       

    def update_grid(self, adj, grid):
        adj.g = grid.g + 10
        adj.h = self.get_heuristic(adj)
        adj.parent = grid
        adj.f = adj.h + adj.g

    def process(self):        
        heapq.heappush(self.opened, (self.start.f, self.start))
        while len(self.opened):
            f, grid = heapq.heappop(self.opened)
            self.closed.add(grid)
            if grid is self.end:
                self.display_path()
                break
            adj_grids = self.get_adjacent_grids(grid)
            for adj_grid in adj_grids:
                if not adj_grid.blocked and adj_grid not in self.closed:
                    if (adj_grid.f, adj_grid) in self.opened:
                        if adj_grid.g > grid.g + 10:
                            self.update_grid(adj_grid, grid)
                    else:
                        self.update_grid(adj_grid, grid)
                        heapq.heappush(self.opened, (adj_grid.f, adj_grid))


def main():
    pygame.init()
    a = Gridworld()
    a.init_grid()
    a.display_grid()
    a.process()
    a.display_grid()
    pygame.quit()
    
if __name__ == '__main__':
    main()
