import random
import heapq


class Str(object):
    def __init__(self):
        self.str = []
        
    def genRandStr(self):
        for i in range(20):
            self.str.append(random.getrandbits(1))

    def retStr(self):
        return self.str

    def evaluate(self, data):
        x=0
        for clause in data:
            x += self.isClause(clause)
        return x

    def isClause(self, clause):
        x=False
        for var in clause:
            if (var =='0'):
                continue;
            if (int(var) > 0):
                x = x or bool(self.str[abs(int(var))-1])
            else:
                x = x or (not(self.str[abs(int(var))-1]))
        return x
    
class Genetic(object):
    def __init__(self, numClauses):
        self.numClauses=numClauses
        self.s = []
        self.cnf = []
        self.sat = bool()
        self.f = []
        self.mutable = []
        self.mutant = []

    def readFile(self):
        filename = "C:\\Users\\sachinsri12345\\Desktop\\AI2\\uf20\\uf20-01.cnf"
        eof = False
        with open(filename) as f:
            for line in f:
                if (eof):
                    self.sat = line[0]
                    continue
                if( line[0] == 'c' or line[0] == 'p' ):
                    continue
                if( line[0] == '%'):
                    eof = True
                    continue
                self.cnf.append(line.split())
        print(self.cnf)

    def elite(self):
        x =[]
##        x.append(heapq.nlargest(2, self.f))
##        self.mutant.append(self.s[self.f.index(x[0][0])].retStr())
##        self.mutant.append(self.s[self.f.index(x[0][1])].retStr())
        #x = heapq.nlargest(2, self.f)
        i,j = map(self.f.index, heapq.nlargest(2, self.f))
        self.mutable.append(self.s[i].retStr())
        self.mutable.append(self.s[j].retStr())
        print(i,j)
        #self.mutable.append(self.s[self.f.index(x[0])].retStr())
        #self.mutable.append(self.s[self.f.index(x[1])].retStr())
        #self.mutable.append(self.s[x[0]].retStr())
        #self.mutable.append(self.s[x[1]].retStr())
        #x = int(self.f.pop[max(int(self.f))])
        #self.index(max(a))
        #print(self.mutable[0])
        #print(self.mutable[1])
            
#    def mutate(self):

 #   def mutate(self):
        
    
    def reproduce(self):
        for i in range(2,10,2):
            child1 = []
            child2 = []
            parent1 = self.mutable[i]
            parent2 = self.mutable[i+1]
            for p1, p2 in zip(parent1,parent2):
                if (random.random()> .5):
                    child1.append(p1)
                    child2.append(p2)
                else:
                    child1.append(p2)
                    child2.append(p1)
            self.mutant.append(child1)
            self.mutant.append(child2)
        print(self.mutant)

    def naturallySelect(self):
        prob = []
        for temp in self.f:
            prob.append(temp/sum(self.f))
        print(prob)
        for j in range(8):
            rand = random.random()
            print(rand)
            chance = 0;
            for i in range(10):
                chance += prob[i]
                if(rand < chance):
                    self.mutable.append(self.s[i].retStr())
                    break
        print(self.mutable)
        #print(sum(prob))
        
    def genRandStrs(self):
        for i in range(10):
            temp = Str()
            temp.genRandStr()
            self.s.append(temp)

    def printRandStrs(self):
        for i in range(10):
            #self.s[i].printStr()
            print(self.s[i].retStr())

    def evaluateAll(self):
        for its in self.s:
            self.f.append(its.evaluate(self.cnf))
        print(self.f)
    
def main():
    A = Genetic(100)
    A.readFile()
    A.genRandStrs()
    A.printRandStrs()
    A.evaluateAll()
    A.elite()
    A.naturallySelect()
    A.reproduce()

if __name__ == '__main__':
    main()
