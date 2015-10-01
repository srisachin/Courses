import random
import heapq
import math
    
class Genetic(object):
    def __init__(self, numClauses, numVars):
        self.numClauses=numClauses
        self.numVars=numVars
        self.cnf = []
        self.sat = bool()
        self.fh = []
        self.fc = []
        self.humans = []
        self.parents = []
        self.children = []
        self.mutants = []

    def genSingleHuman(self):
        h = []
        for i in range(20):
            h.append(random.getrandbits(1))
        return h
    
    def evaluate(self, h, data):
        x=0
        for clause in data:
            x += self.isClause(h, clause)
        return x+random.random()

    def isClause(self, h, clause):
        x=False
        for var in clause:
            if (var =='0'):
                continue;
            if (int(var) > 0):
                x = x or bool(h[abs(int(var))-1])
            else:
                x = x or (not(h[abs(int(var))-1]))
        return x

    def makeRandStrs(self):
        for i in range(10):
            self.humans.append(self.genSingleHuman())

    def printRandStrs(self):
        for i in range(10):
            print(self.humans[i])

    def evaluateHumans(self):
        self.fh = []
        for h in self.humans:
            self.fh.append(self.evaluate(h, self.cnf))
        print(self.fh)

    def evaluateChildren(self):
        self.fc = []
        for c in self.children:
            self.fc.append(self.evaluate(c, self.cnf))
        print(self.fc)
        
    def readFile(self):
        filename = "C:\\Users\\sachinsri12345\\Desktop\\AI2\\uf20\\uf20-01.cnf"
        eof = False
        with open(filename) as foo:
            for line in foo:
                if (eof):
                    self.sat = line[0]
                    continue
                if( line[0] == 'c' or line[0] == 'p' ):
                    continue
                if( line[0] == '%'):
                    eof = True
                    continue
                self.cnf.append(line.split())
 #       print(self.cnf)

    def elite(self):
        i,j = map(self.fh.index, heapq.nlargest(2, self.fh))
        self.mutants.append(self.humans[i])
        self.mutants.append(self.humans[j])
#        print(i,j)            
    
    def multiply(self):
        for i in range(0,8,2):
            child1 = []
            child2 = []
            parent1 = self.parents[i]
            parent2 = self.parents[i+1]
            for p1, p2 in zip(parent1,parent2):
                if (random.random()> .5):
                    child1.append(p1)
                    child2.append(p2)
                else:
                    child1.append(p2)
                    child2.append(p1)
            self.children.append(child1)
            self.children.append(child2)
#        print(*self.children, sep='\n')

    def naturallySelect(self):
        prob = []
        for temp in self.fh:
            prob.append(temp/sum(self.fh))
#        print(prob)
        for j in range(8):
            rand = random.random()
#            print(rand)
            chance = 0;
            for i in range(10):
                chance += prob[i]
                if(rand < chance):
                    self.parents.append(self.humans[i])
                    break
#        print(*self.parents, sep='\n')
        
    def mutate(self):
        counter = 0
        for c in self.children:
            index = 0
            if (random.random() > .7):
                for i in c:
                    if (random.random() > .5):
                        c[index] = int(not c[index])
                        counter +=1
                    index += 1
                        
#        print(counter)
#        print(*self.children, sep='\n')

    def flip(self):
        for c in self.children:
            oldEval = math.floor(self.evaluate(c, self.cnf))
            flag = 1
            while (flag):
                flag = 0
                index = 0
                for i in c:
                    c[index] = int(not c[index])
                    newEval = math.floor(self.evaluate(c, self.cnf))
                    #print(newEval, oldEval)
                    if( newEval > oldEval):
                        oldEval= newEval
                        flag = 1
                    else:
                        c[index] = int(not c[index])
                    index += 1
                        
#        print(*self.children, sep='\n')
    
    def makeNewPopulation(self):
        for c in self.children:
            self.mutants.append(c)
        self.humans = self.mutants
#        print(*self.humans, sep = '\n')
#        print('\n')
#        print(*self.mutants, sep = '\n')
    
def main():
    A = Genetic(100, 20)
    A.readFile()
    A.makeRandStrs()
    A.printRandStrs()
    A.evaluateHumans()
    
    A.elite()
    A.naturallySelect()
    A.multiply()
    A.mutate()
    print('After Mutate\n')
    A.evaluateChildren()
    A.flip()
    print('After Flip\n')
    A.evaluateChildren()
    A.makeNewPopulation()
    print('New Pop\n')
    A.evaluateHumans()
    
if __name__ == '__main__':
    main()
