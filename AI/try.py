import random

def find_path(graph, start, end, path=[]):
       path = path + [start]
       if start == end:
           return path
       if start not in graph:
           return None
       for node in graph[start]:
           if node not in path:
               newpath = find_path(graph, node, end, path)
               if newpath: return newpath
       return None

def recursive_dfs(graph, start, path=[]):
    if start not in graph:
        return path
    
    #del graph[start]
    #return path
    #else:
    path=path+[start]
    if random.randint(1,10) > 7:
        for i in graph[start].keys():
            graph[start][i]=999;
            graph[i][start]=999;
    for node in graph[start]:
        if not node in path:
            path=recursive_dfs(graph, node, path)
    return path

def main():
    graph = {1:{2:1,11:1}}
    graph.update({10:{9:1,20:1}})
    graph.update({91:{92:1,81:1}})
    graph.update({100:{99:1,90:1}})
    graph.update({1+x:{1+x+1:1, 1+x+10:1, 1+x-10:1} for x in range(10,90,10)})
    graph.update({10+x:{10+x-1:1, 10+x+10:1, 10+x-10:1} for x in range(10,90,10)})
    graph.update({1+x:{1+x-1:1, 1+x+1:1, 1+x+10:1} for x in range(1,9)})
    graph.update({91+x:{91+x-1:1, 91+x+1:1, 91+x-10:1} for x in range(1,9)})
    
    for y in range(11,91,10):
        graph.update({x+y: {x+y-1:1, x+y+1:1, x+y+10:1, x+y-10:1} for x in range(1,9)})
    
    #path = find_path(graph, 2, 8)
    for i in range(1,100):
        print((i,graph[i]), sep=' ')
    path = recursive_dfs(graph, 2)
    for i in range(1,100):
        print((i,graph[i]), sep=' ')
       #print(path[:])
    #graph[32]=999;
    #print(path)
    
if __name__ == '__main__':
    main()
