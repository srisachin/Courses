import heapq
import random
import pygame

BLACK    = (   0,   0,   0)
WHITE    = ( 255, 255, 255)
GREEN    = (   0, 255,   0)
RED      = ( 255,   0,   0)
BLUE     = ( 0,   0,   255)
 
width  = 6
height = 6
margin = 1
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
        self.open = []
        heapq.heapify(self.open)
        self.close = set()
        self.grids = []
        self.grid_height = 101
        self.grid_width = 101
        temp_path = []
        agent_world =[]
    
    def init_grid(self):
        for x in range(self.grid_width):
            for y in range(self.grid_height):
                blocked = False
                if random.randint(1,100) < 20:
                    blocked = True
                self.grids.append(Grid(x, y, blocked))
        blocked = False
        for x in range(self.grid_width):
            for y in range(self.grid_height):
                self.agent_world.append(Grid(x, y, blocked))
                
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

    def expand_grid(self, grid):
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
        temp_path[:] = [] 
        while grid.parent is not self.start:
            temp_path.append(grid)
            grid = grid.parent
            grid.path = True
            print('path: grid: %d,%d, %r' % (grid.x, grid.y, grid.blocked))
        
    def first_blocked(self)
        for node in temp_path
            if(node.blocked == True)
                print("Blocked Node %d, %d",node.x, node.y)
                
    def display_grid(self):
        pygame.display.set_caption("A star implementation")
        screen.fill(BLACK)
        done = False
        while done == False:
            for event in pygame.event.get(): 
                if event.type == pygame.QUIT: 
                    done = True
                elif event.type == pygame.MOUSEBUTTONDOWN:
                    done = True
                for grid in self.grids:
                        color = WHITE
                        if grid.blocked == False:
                            color = GREEN
                        if grid.path == True:
                            color = RED
                        pygame.draw.rect(screen, color,
                             [(margin+width)*grid.x+margin, (margin+height)*grid.y+margin,
                              width, height])
                    
            pygame.display.flip()
       

    def update_grid(self, exp, grid):
        exp.g = grid.g + 10
        exp.h = self.get_heuristic(exp)
        exp.parent = grid
        exp.f = exp.h + exp.g

    def run(self):        
        heapq.heappush(self.open, (self.start.f, self.start))
        while len(self.open):
            f, grid = heapq.heappop(self.open)
            self.close.add(grid)
            if grid is self.end:
                self.display_path()
                break
            exp_grids = self.expand_grid(grid)
            for exp_grid in exp_grids:
                if not exp_grid.blocked and exp_grid not in self.close:
                    if (exp_grid.f, exp_grid) in self.open:
                        if exp_grid.g > grid.g + 10:
                            self.update_grid(exp_grid, grid)
                    else:
                        self.update_grid(exp_grid, grid)
                        heapq.heappush(self.open, (exp_grid.f, exp_grid))


def main():
    pygame.init()
    a = Gridworld()
    a.init_grid()
    a.display_grid()
    a.run()
    a.display_grid()
    pygame.quit()
    
if __name__ == '__main__':
    main()
