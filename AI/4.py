import heapq
import random
import pygame

# Define some colors
BLACK    = (   0,   0,   0)
WHITE    = ( 255, 255, 255)
GREEN    = (   0, 255,   0)
RED      = ( 255,   0,   0)
BLUE     = ( 0,   0,   255)
 
# This sets the width and height of each grid location
width  = 20
height = 20
 
# This sets the margin between each cell
margin = 5

# Set the height and width of the screen
size = [280, 280]

    
screen = pygame.display.set_mode(size)
 
class Cell(object):
    def __init__(self, x, y, reachable):
        """
        Initialize new cell

        @param x cell x coordinate
        @param y cell y coordinate
        @param reachable is cell reachable? not a wall?
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
        self.cells = []
        self.grid_height = 11
        self.grid_width = 11

    def init_grid(self):
        for x in range(self.grid_width):
            for y in range(self.grid_height):
                reachable = True
                if random.randint(1,10) < 3:
                    reachable = False
                """if x==1 and y==1:
                    reachable = True
                if x==self.grid_width-1 and y==self.grid_height-1:
                    reachable = True"""
                self.cells.append(Cell(x, y, reachable))
        self.start = self.get_cell(0, 0)
        self.end = self.get_cell(10, 10)
        self.start.reachable = True
        self.end.reachable = True
        self.start.path = True
        self.end.path = True

    def get_heuristic(self, cell):
        """
        Compute the heuristic value H for a cell: distance between
        this cell and the ending cell multiply by 10.

        @param cell
        @returns heuristic value H
        """
        return 10 * (abs(cell.x - self.end.x) + abs(cell.y - self.end.y))

    def get_cell(self, x, y):
        """
        Returns a cell from the cells list

        @param x cell x coordinate
        @param y cell y coordinate
        @returns cell
        """
        return self.cells[x * self.grid_height + y]

    def get_adjacent_cells(self, cell):
        """
        Returns adjacent cells to a cell. Clockwise starting
        from the one on the right.

        @param cell get adjacent cells for this cell
        @returns adjacent cells list 
        """
        cells = []
        if cell.x < self.grid_width-1:
            cells.append(self.get_cell(cell.x+1, cell.y))
        if cell.y > 0:
            cells.append(self.get_cell(cell.x, cell.y-1))
        if cell.x > 0:
            cells.append(self.get_cell(cell.x-1, cell.y))
        if cell.y < self.grid_height-1:
            cells.append(self.get_cell(cell.x, cell.y+1))
        return cells

    def display_path(self):
        cell = self.end
        while cell.parent is not self.start:
            cell = cell.parent
            cell.path = True
            print('path: cell: %d,%d' % (cell.x, cell.y))
        
        
    def display_grid(self):
        pygame.display.set_caption("A star implementation")
        screen.fill(BLACK)
        done = False
        while done == False:
            for event in pygame.event.get(): # User did something
                if event.type == pygame.QUIT: # If user clicked close
                    done = True
                else:
            # Draw the grid
                    for cell in self.cells:
                        color = WHITE
                        if cell.reachable == True:
                            color = GREEN
                        if cell.path == True:
                            color = RED
                        pygame.draw.rect(screen, color,
                             [(margin+width)*cell.x+margin, (margin+height)*cell.y+margin,
                             #[(margin+width)*cell.x, (margin+height)*cell.y,
                              width, height])
                    
            pygame.display.flip()
        pygame.quit()
            
    """def compare(self, cell1, cell2):
        ""
        Compare 2 cells F values

        @param cell1 1st cell
        @param cell2 2nd cell
        @returns -1, 0 or 1 if lower, equal or greater
        ""
        if cell1.f < cell2.f:
            return -1
        elif cell1.f > cell2.f:
            return 1
        return 0
    """
    def update_cell(self, adj, cell):
        """
        Update adjacent cell

        @param adj adjacent cell to current cell
        @param cell current cell being processed
        """
        adj.g = cell.g + 10
        adj.h = self.get_heuristic(adj)
        adj.parent = cell
        adj.f = adj.h + adj.g

    def process(self):        
        # add starting cell to open heap queue
        heapq.heappush(self.opened, (self.start.f, self.start))
        while len(self.opened):
            # pop cell from heap queue 
            f, cell = heapq.heappop(self.opened)
            # add cell to closed list so we don't process it twice
            self.closed.add(cell)
            # if ending cell, display found path
            if cell is self.end:
                self.display_path()
                break
            # get adjacent cells for cell
            adj_cells = self.get_adjacent_cells(cell)
            for adj_cell in adj_cells:
                if adj_cell.reachable and adj_cell not in self.closed:
                    if (adj_cell.f, adj_cell) in self.opened:
                        # if adj cell in open list, check if current path is
                        # better than the one previously found
                        # for this adj cell.
                        if adj_cell.g > cell.g + 10:
                            self.update_cell(adj_cell, cell)
                    else:
                        self.update_cell(adj_cell, cell)
                        # add adj cell to open list
                        heapq.heappush(self.opened, (adj_cell.f, adj_cell))


def main():
    pygame.init()
 

    # Set title of screen

    a = AStar()
    a.init_grid()
    #a.display_grid()
    a.process()
    a.display_grid()
   
    
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
