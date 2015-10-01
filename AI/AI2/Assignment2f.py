import random
import heapq
import math
    
class Genetic(object):
    def __init__(self):
        self.numClauses = 0
        self.numVars = 0
        self.cnf = []
        self.sat = bool()
        self.fh = []
        self.fc = []
        self.humans = []
        self.parents = []
        self.children = []
        self.mutants = []
##        self.variables = 0
##        self.clauses = 0

# Produce a random boolean assignment in the string format
    def genSingleHuman(self):
        h = []
        for i in range(self.numVars):
            h.append(random.getrandbits(1))
        return h

#Evaluate f value of the string
    def evaluate(self, h, data):
        x=0
        for clause in data:
            x += self.isClause(h, clause)
        return x+random.random()

#Check if the clause is satisfiable
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

#Make random population of Strings
    def makeRandStrs(self):
        for i in range(10):
            self.humans.append(self.genSingleHuman())


    def printRandStrs(self):
        for i in range(10):
            print(self.humans[i])

#Evaluate the f value of the population
    def evaluateHumans(self):
        del self.fh[:]
        for h in self.humans:
            self.fh.append(self.evaluate(h, self.cnf))
        #print(self.fh)

#Evaluate the f value of Children
    def evaluateChildren(self):
        del self.fc[:]
        for c in self.children:
            self.fc.append(self.evaluate(c, self.cnf))
        #print(self.fc)

#Read CNF File
    def readFile(self):
        print("Select Number of Variables")
        self.numVars = int(input("20, 50, 75 or 100: "))
        print("Select File Number")
        fileNo = int(input("Select between 1 to 100: "))

        print("")
        if self.numVars == 20:
            filename = 'input\\uf20-91\\uf20-0'+str(fileNo)+'.cnf'
            self.numClauses = 91
        if self.numVars == 50:
            filename = 'input\\uf50-218\\uf50-0'+str(fileNo)+'.cnf'
            self.numClauses = 218
        if self.numVars == 75:
            filename = 'input\\uf75-325\\uf75-0'+str(fileNo)+'.cnf'
            self.numClauses = 325
        if self.numVars == 100:
            filename = 'input\\uf100-430\\uf100-0'+str(fileNo)+'.cnf'
            self.numClauses = 430
        
##        filename = "input\\uf50-218\\uf50-03.cnf"
        eof = False
        with open(filename) as foo:
            for line in foo:
                if (eof):
                    self.sat = line[0]
                    break
                if( line[0] == 'c' or line[0] == 'p' ):
                    continue
                if( line[0] == '%'):
                    eof = True
                    continue
                self.cnf.append(line.split())
 #       print(self.cnf)

#Select two strings with highest value of f
    def elite(self):
        i,j = map(self.fh.index, heapq.nlargest(2, self.fh))
        self.mutants.append(self.humans[i])
        self.mutants.append(self.humans[j])
#        print(i,j)            

#Select the individual strings for perpforming the crossover
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

#Perform the crossover
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

#Perform the mutation
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

#Perform flip operation
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

#Copy new string to the orginal data structure
    def makeNewPopulation(self):
        for c in self.children:
            self.mutants.append(c)
        del self.humans[:]
        self.humans = self.mutants[:]
        del self.children[:]
        del self.parents[:]
        del self.mutants[:]
#        print(*self.humans, sep = '\n')
#        print('\n')
#        print(*self.mutants, sep = '\n')

#The main loop of the FlipGA algorithm
    def evolution(self):
        self.evaluateHumans()
        for i in range(100):
            if(max(self.fh) > self.numClauses):
                break
            self.evaluateHumans()
            self.elite()
            self.naturallySelect()
            self.multiply()
            self.mutate()
                #print('After Mutate\n')
            self.evaluateChildren()
            self.flip()
                #print('After Flip\n')
            self.evaluateChildren()
            self.makeNewPopulation()
            self.evaluateHumans()
                #print('New Pop\n')
##        print(math.floor(max(self.fh)))
        print("All the ",self.numClauses," clauses were satisfied by gene : ",(self.fh.index(max(self.fh))+1))

    
def main():
    A = Genetic()
    A.readFile()
    A.makeRandStrs()
    #A.printRandStrs()
    #A.evaluateHumans()
    A.evolution()
##    A.elite()
##    A.naturallySelect()
##    A.multiply()
##    A.mutate()
##    print('After Mutate\n')
##    A.evaluateChildren()
##    A.flip()
##    print('After Flip\n')
##    A.evaluateChildren()
##    A.makeNewPopulation()
##    print('New Pop\n')
##    A.evaluateHumans()
    
if __name__ == '__main__':
    main()
