import math
   
def NN(examples):
    w0 = 0.2
    w1 = 1
    w2 = -1
    n= .75
    flag=0
    
    for k in range(1,40000):
        flag=0
        for i in examples:
            inputs = w0 + w1 * i[0] + w2 * i[1]
            if(i[2]*inputs <= 0):
                w0 = w0 + n * i[2] * 1 
                w1 = w1 + n * i[2] * i[0] 
                w2 = w2 + n * i[2] * i[1]
                flag=1
        if(flag==0):
            break

    print( w0, w1, w2, k, sep=' ' )
            
    
def main():
    examples = [
                [0.01, 0.5, -1],
                [0.1, 0.7, 1],
                [0.15, 1, -1],
                [0.2, 0.5, 1],
                [0.25, 0.3, 1],
                [0.35, 0.35, 1],
                [0.4, 0.75, -1],
                [0.45, 0.5, 1],
                [0.55, 0.2, -1],
                [0.7, 0.65, -1],
                [0.8, 0.25, -1],
                [0.95, 0.45, -1]
                ]
    NN(examples)


if __name__ == '__main__':
    main()
