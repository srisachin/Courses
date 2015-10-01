import math
   
def NN(examples):
    w0 = 2
    w1 = 2
    n= 0.005
    flag=0
    
    for k in range(1,500):
        flag=0
        for i in examples:
            inputs = w0 + w1 * i[0]
         #   inputs = w0*i[0]
            if(i[1]*inputs <= 0):
                w0 = w0 + n * i[1] * 1 
                w1 = w1 + n * i[1] * i[0] 
                flag=1
        if(flag==0):
            break

    print( w0, w1, k, sep=' ' )
            
    
def main():
    examples = [
                [.1, 1],
                [.1, -1],
                [.3, 1],
                [.35,-1],
                [.45, 1],
                [.6, 1],
                [.7, -1],
                [.95, -1],
                ]
    NN(examples)


if __name__ == '__main__':
    main()
