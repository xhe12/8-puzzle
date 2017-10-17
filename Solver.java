package puzzle;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;


public class Solver {
    private final SearchNode puzzleNode;
    private final SearchNode twinPuzzleNode;
    private SearchNode key;
    private MinPQ<SearchNode> pq;
    private int number;
    
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        number = 0;
        key = new SearchNode(initial,0);
        key.previous = key;
        puzzleNode = key;
        twinPuzzleNode = new SearchNode(puzzleNode.board.twin(), 0);
        twinPuzzleNode.previous = twinPuzzleNode;
        pq = new MinPQ<SearchNode>(new pOrder());
        pq.insert(key);
        Iterable<Board> neighbor = new Queue<Board>();
        if(!isSolvable()) return;
        for(int k = 0; k<3; k++)
        while(!key.board.isGoal())
        {
            key = pq.delMin();
            neighbor = key.board.neighbors();
            for(Board i: neighbor)
            {
                if(!i.equals(key.previous.board))
                {
                    SearchNode insertion = new SearchNode(i,key.moves+1);
                    insertion.previous = key;
                    pq.insert(insertion);
                }
            }
            

        }
        number = key.moves;
        
        
    }
    
    private class SearchNode
    {
        private Board board;
        private int moves;
        private int mDistance;
        private SearchNode previous;
        private SearchNode root;

        public SearchNode(Board board, int moves)
        {
            this.board = board;
            this.moves = moves;
            mDistance = board.manhattan();
            this.previous = this;
            this.root = this;
        }
    }
    
    
    
    private static class pOrder implements Comparator<SearchNode>
    {
        public int compare(SearchNode a, SearchNode b)
        {
            if (a.mDistance+a.moves>b.mDistance+b.moves) return 1;
            else if(a.mDistance+a.moves<b.mDistance+b.moves) return -1;
            else return 0;
        }
    }
    
    public boolean isSolvable()            // is the initial board solvable?
    {
        MinPQ<SearchNode> pqPuzzle = new MinPQ<SearchNode>(new pOrder());
        SearchNode searchKey;
        Iterable<Board> neighbor = new Queue<Board>();
        pqPuzzle.insert(puzzleNode);
        pqPuzzle.insert(twinPuzzleNode);
        while(true)
        {
            
            searchKey = pqPuzzle.delMin();
            if(searchKey.board.isGoal()&&searchKey.root==puzzleNode)
            {
                return true;
            }
            if(searchKey.board.isGoal()&&searchKey.root==twinPuzzleNode)
            {
                return false;
            }
            neighbor = searchKey.board.neighbors();
            for(Board i: neighbor)
            {
                if(!i.equals(searchKey.previous.board))
                {
                    SearchNode node = new SearchNode(i,searchKey.moves+1);
                    node.root = searchKey.root;
                    node.previous = searchKey;
                    pqPuzzle.insert(node);
                }
            }
            
        }

        
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        if(isSolvable())
        {
            return number;
        }
        else return -1;
    }
    
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if(isSolvable())
        {
            Stack<Board> steps = new Stack<Board>();
            
            while(key!=key.previous)
            {
                steps.push(key.board);
                key = key.previous;
                
            }
            steps.push(puzzleNode.board);
            return steps;
        }
        else return null;
    }
    
    

   public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());

        for (Board board : solver.solution())
        {
            StdOut.println(board.toString());

        }
    }
    
    StdOut.println(solver.moves());




}
   

}