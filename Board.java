package puzzle;
import edu.princeton.cs.algs4.Queue;



public class Board{
    private int[][] blocks;
    private int dim;
    private int mDistance;
    private int indexRow;//row of the empty block;
    private int indexColumn;//column of the empty block;
    
    
    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    {
        
        dim = blocks.length;
        this.blocks = new int[dim][dim];
        this.blocks = blocks;
        this.mDistance = manhattan();
        
    }
    private Board(Board board)
    {
        
        this.dim = board.dim;
        this.blocks = new int[dim][dim];
        for (int i = 0; i < dim; i++)
        {
            for(int j = 0; j<dim; j++)
            {
                this.blocks[i][j] = board.blocks[i][j];
            }
        }
        this.mDistance = board.mDistance;
        this.indexRow = board.indexRow;
        this.indexColumn = board.indexColumn;
        
        //this.hDistance = hamming(this.blocks);
    }
    
    
    public int dimension()                 // board dimension n
    {
        return dim;
    }
    public int hamming()                   // number of blocks out of place
    {
        int sum = 0;
        for (int i = 0; i <dim; i++)
        {
            for(int j = 0; j<dim; j++)
            {
                if( i*dim+j+1 != blocks[i][j])
                {
                    sum++;
                }
            }
        }
        
        return sum-1;//Exclude the empty element 0;
    }
    
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        if (this.mDistance!=0)
            return this.mDistance;
        int sum = 0;
        for(int i = 0; i<dim; i++)
        {
            for(int j=0; j<dim; j++)
            {
                if(blocks[i][j]!=0)
                {
                   int row = (blocks[i][j]-1)/dim;
                   int col = (blocks[i][j]-1)-row*dim;
                   sum += Math.abs(i-row)+ Math.abs(j-col); 
                }
                else
                {
                    this.indexRow = i;
                    this.indexColumn = j;
                }
                   
            }
        }
        return sum;
    }
    
    
    
    public boolean isGoal()                // is this board the goal board?
    {
        
        if(mDistance==0) return true;
        else return false;
    }
    
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        Board temp = new Board(this);
        
        if(indexRow!=0)
        {
            exch(temp.blocks, 0, 0, 0, 1);
            temp.mDistance += mTwinDifference(this.blocks,0,0,0,1);
        }
        else
        {
            exch(temp.blocks, 1, 0, 1, 1);
            temp.mDistance += mTwinDifference(this.blocks,1, 0, 1, 1);
        }
        return temp;
        
    }
    
    private int mTwinDifference(int[][] blocks, int i1,int j1,int i2,int j2)   //Calculate the Manhattan Distance Difference when (i1,j1) and (i2,j2) are swapped
    {
        int md1;
        int md2;
        int diff;
        
        int row1 = (blocks[i1][j1]-1)/dim;
        int col1 = blocks[i1][j1]-row1*dim-1;
        int row2 = (blocks[i2][j2]-1)/dim;
        int col2 = blocks[i2][j2]-row2*dim-1;
        md1 = Math.abs(j1-col1)+Math.abs(j2-col2);
        md2 = Math.abs(j2-col1)+Math.abs(j1-col2);
        diff = md2-md1;
        return diff;
    }

    
    
    private void exch(int[][] blocks, int i1, int j1, int i2, int j2)
    {
        int temp;
        temp = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = temp;
    }
    
    public boolean equals(Object y)        // does this board equal y?
    {
        
        if(y==null||y.getClass()!=this.getClass()) return false;
        
        Board that = (Board) y;
        if (this.blocks.length!=that.blocks.length)
            return false;
        for(int i = 0; i<dim; i++)
        {
            for(int j = 0; j<dim; j++)
            {
                if(that.blocks[i][j]!=this.blocks[i][j])
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Queue<Board> queue = new Queue<Board>();
        Board neighbor;
        int row;
        int col;
        
        if(indexRow!=0)
        {
            neighbor = new Board(this);
            exch(neighbor.blocks, indexRow,indexColumn, indexRow-1, indexColumn);
            row = (neighbor.blocks[indexRow][indexColumn]-1)/dim;
            
            neighbor.mDistance = this.mDistance + Math.abs(indexRow-row) - Math.abs(indexRow-1-row);
            neighbor.indexRow -= 1;
            queue.enqueue(neighbor);
        }
        
        if(indexColumn!=0)
        {
            neighbor = new Board(this);
            exch(neighbor.blocks, indexRow,indexColumn, indexRow, indexColumn-1);
            row = (neighbor.blocks[indexRow][indexColumn]-1)/dim;
            col = neighbor.blocks[indexRow][indexColumn]-row*dim-1;
            
            neighbor.mDistance = this.mDistance +Math.abs(indexColumn-col) - Math.abs(indexColumn-1-col);
            neighbor.indexColumn -= 1;
            queue.enqueue(neighbor);
        }       
        if(indexRow!=dim-1)
        {
            neighbor = new Board(this);
            exch(neighbor.blocks, indexRow,indexColumn, indexRow+1, indexColumn);
            row = (neighbor.blocks[indexRow][indexColumn]-1)/dim;
            
            neighbor.mDistance =this.mDistance + Math.abs(indexRow-row) - Math.abs(indexRow+1-row);
            neighbor.indexRow += 1;
            queue.enqueue(neighbor);
        }
        if(indexColumn!=dim-1)
        {
            neighbor = new Board(this);
            exch(neighbor.blocks, indexRow,indexColumn, indexRow, indexColumn+1);
            row = (neighbor.blocks[indexRow][indexColumn]-1)/dim;
            col = neighbor.blocks[indexRow][indexColumn]-row*dim-1;
            
            neighbor.mDistance = this.mDistance +Math.abs(indexColumn-col) - Math.abs(indexColumn+1-col);
            neighbor.indexColumn += 1;
            queue.enqueue(neighbor);
        }
        
        return queue;
        
    }
    

    
    public String toString()               // string representation of this board (in the output format specified below)
    {
        String s;
        s =String.format("%2d",dim) + "\n";
        for (int i = 0; i < dim; i++)
        {
            for(int j = 0; j < dim; j++)
            {
                s = s +String.format("%2d ", blocks[i][j]);
                if (j == dim-1)
                    s += "\n";
            }
        }
        return s;
    }


}




