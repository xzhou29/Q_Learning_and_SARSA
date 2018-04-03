package rl;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * @author XinZhou
 */
public class PD extends JFrame {

    //<editor-fold defaultstate="collapsed" desc="Variables declaration">
    private int PD_SIZE = 100;
    public static int current_x = 1, current_y = 5; // current position
    // pick up postions: [1][1]  [3][3]  [4][1]  [5][5]
    private int block_size = 4;
    private int numberOfBlocksOn_1 = block_size, numberOfBlocksOn_2 = block_size,
            numberOfBlocksOn_3 = block_size, numberOfBlocksOn_4 = block_size ; // Each of them has 4 block  21 22 23 24
    
    // drop off positions: [4][4]  [5][1]
    private int dropOffPlace_1 = 0, dropOffPlace_2 = 0; // Drop off position -- limit 8  31 32
    private boolean hasFlower = false;
    private int previousColor = 0; // The color was replace by agent 9
    private int previousOperation = 0; // The color was replace by agent 9
    
    public static int maxSteps = 6000;
    public static int learningMethod = 1; //1.Q learning 2.SARSA q learning
    public static int policyType = 1; //1.PRANDOM  2.PEPLOIT  3.PGREEDY
    
    public static boolean isPickUpEmpty = false;
    public static boolean isDropOffFull = false;
    public static int timeDelay = 1; //ms
//</editor-fold>
    
    /**
     * maze[][]
     * 
     * Values: 0 = empty
     *         1 = wall
     *         21 22 23 24 = pick up place
     *         31 32 = drop off place
     *         9 = agent 
     * 
     */
    
    //<editor-fold defaultstate="collapsed" desc="maze array">
    public int [][] maze =
    {   {1,1,1,1,1,1,1},
        {1,21,0,0,0,9,1},
        {1,0,0,0,0,0,1},
        {1,0,0,22,0,0,1},
        {1,23,0,0,31,0,1},
        {1,32,0,0,0,24,1},
        {1,1,1,1,1,1,1}
    };
//</editor-fold>
    
    public PD() {
        setTitle("PD WORLD");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

    @Override
    public void paint(Graphics g) {
        
        super.paint(g);
        g.translate(50, 50);
        // draw the maze
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                Color color;
                switch (maze[row][col]) {
                    case 1 : color = Color.BLACK; break; //wall
                    case 21 : color = Color.YELLOW; break; //flower
                    case 22 : color = Color.YELLOW; break; //flower
                    case 23 : color = Color.YELLOW; break; //flower
                    case 24 : color = Color.YELLOW; break; //flower
                    case 31 : color = Color.GREEN; break; // drop off
                    case 32 : color = Color.GREEN; break; // drop off
                    case 9 : color = Color.RED; break; // agent
                    case 10 : color = Color.BLUE; break; // Drop of is full
                    default : color = Color.WHITE; // empty
                }
                g.setColor(color);
                g.fillRect(PD_SIZE * col, PD_SIZE * row, PD_SIZE, PD_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(PD_SIZE * col, PD_SIZE * row, PD_SIZE, PD_SIZE);
            }
        }
       
    }
    
    public void move(int nextDirection){
        
            if (nextDirection == 1) { // up
                maze[current_x][current_y] = previousColor;  
                previousColor = maze[current_x - 1][current_y];
                maze[current_x - 1][current_y] = 9;  
                current_x -= 1;
            }
            else if (nextDirection == 2) { //down
                maze[current_x][current_y] = previousColor;  
                previousColor = maze[current_x + 1][current_y];
                maze[current_x + 1][current_y] = 9;  
                current_x += 1; 
            }
            else if (nextDirection == 3) { //left
                maze[current_x][current_y ] = previousColor;  
                previousColor = maze[current_x][current_y - 1];
                maze[current_x][current_y - 1] = 9;  
                current_y -= 1;
            }
            else if (nextDirection == 4) { //right
                maze[current_x][current_y ] = previousColor;  
                previousColor = maze[current_x][current_y + 1];
                maze[current_x][current_y + 1] = 9;  
                current_y += 1;
            }
    }
    
    public static void printBankAccount(String bankAccountOutput) throws FileNotFoundException, UnsupportedEncodingException{
        String a = "";
        String b = "";
        if(learningMethod == 1){
            a = "Q_Learning";
        }else if(learningMethod == 2){
            a = "SARSA";
        }
        if(policyType == 1){
            b = "PRANDOM";
        }else if(policyType == 2){
            b = "PEXPLOIT";
        }else if(policyType == 3){
            b = "PGREEDY";
        }
        PrintWriter writer = new PrintWriter(a + "_" + b + "_bankAccount" + maxSteps+ ".txt", "UTF-8");
        System.out.print(bankAccountOutput);
        writer.print( bankAccountOutput );
        writer.close();
    }
    
    public static void input(){
        Scanner sc = new Scanner(System.in);
        int input;
        do {
            System.out.println("Choose RL Algrithm(1.Q-learning 2.SARSA ): ");
            while (!sc.hasNextInt()) {
                System.out.println("Wrong input!!! Choose RL Algrithm(1.Q-learning 2.SARSA ):  ");
                sc.next(); 
            }
            input = sc.nextInt();
        } while (input < 1 || input > 2);
        learningMethod = input; //1.Q learning 2.SARSA q learning
        
        input = 0;
        do {
            System.out.println("Choose a policy (1.PRANDOM  2.PEPLOIT  3.PGREEDY):  ");
            while (!sc.hasNextInt()) {
                System.out.println("Wrong input!!! Choose a policy (1.PRANDOM  2.PEPLOIT  3.PGREEDY): ");
                sc.next();
            }
            input = sc.nextInt();
        } while (input < 1 || input > 3);
        policyType = input; //1.PRANDOM  2.PEPLOIT  3.PGREEDY
        input = 0; 
        do {
            System.out.println("Input a number for maxmium steps to terminate (1000 - 30000):  ");
            while (!sc.hasNextInt()) {
                System.out.println("Wrong input!!! Input a number for maxmium steps to terminate (1000 - 30000): ");
                sc.next();
            }
            input = sc.nextInt();
        } while (input < 1000 || input > 30000);
        maxSteps = input; 
        input = 0; 
        
        do {
            System.out.println("Choose the speed of moving: 1.very fast 2.normal 3.slow  ");
            while (!sc.hasNextInt()) {
                System.out.println("Wrong input!!! Choose the speed of moving: 1.very fast 2.normal 3.slow ");
                sc.next();
            }
            input = sc.nextInt();
        } while (input < 1 || input > 3);
        switch (input) {
            case 1:
                timeDelay = 1;
                break;
            case 2:
                timeDelay = 30;
                break;
            case 3:
                timeDelay = 100;
                break;
            default:
                break;
        }
        input = 0; 
        
        System.out.println("Running... ");

    }
 
    public int action() { 
        int actionType = 0; // 5. pick up -- 6. drop off
        if(!hasFlower){
            int currentPosition = previousColor;
            if( (currentPosition == 21 ) && (numberOfBlocksOn_1 > 0) ){
                numberOfBlocksOn_1 -= 1;
                hasFlower = true;
                if(numberOfBlocksOn_1 == 0){
                    previousColor = 0;
                    isPickUpEmpty = true;
                }
                //System.out.println("pifkc up " + currentPosition);
                actionType = 5;
            }
            else if( (currentPosition == 22 ) && (numberOfBlocksOn_2 > 0) ){
                numberOfBlocksOn_2 -= 1;
                hasFlower = true;
                if(numberOfBlocksOn_2 == 0){
                    previousColor = 0;
                    isPickUpEmpty = true;
                }
                //System.out.println("pifkc up " + currentPosition);
                actionType = 5;
            }
            else if( (currentPosition== 23 ) && (numberOfBlocksOn_3 > 0) ){
                numberOfBlocksOn_3 -= 1;
                 hasFlower = true;
                if(numberOfBlocksOn_3 == 0){
                    previousColor = 0;
                    isPickUpEmpty = true;
                }
                //System.out.println("pifkc up " + currentPosition);
                actionType = 5;
            }
            else if( (currentPosition == 24 ) && (numberOfBlocksOn_4 > 0) ){
                numberOfBlocksOn_4 -= 1;
                hasFlower = true;
                if(numberOfBlocksOn_4 == 0){
                    previousColor = 0;
                    isPickUpEmpty = true;
                }
                //System.out.println("pifkc up " + currentPosition);
                actionType = 5;
            }
        }
        if(hasFlower){
            int currentPosition = previousColor;
            if( (currentPosition == 31 ) && (dropOffPlace_1 < block_size * 2) ){
                hasFlower = false;
                dropOffPlace_1++;
                if(dropOffPlace_1 == block_size * 2){
                    previousColor = 10;
                    isDropOffFull = true;
                }
                //System.out.println("Drop off " + currentPosition);
                actionType = 6;
            }
            else if( (currentPosition == 32 ) && (dropOffPlace_2 < block_size * 2) ){
                hasFlower = false;
                dropOffPlace_2++;
                if(dropOffPlace_2 == block_size * 2){
                    previousColor = 10;
                    isDropOffFull = true;
                }
                //System.out.println("Drop off " + currentPosition);
                actionType = 6;
            }
        } 
        return actionType;
    }
    
    public int check_action(){
            int availableAction = 0; // 5. pick up -- 6. drop off
            if(!hasFlower){
                int currentPosition = previousColor;
                if( (currentPosition == 21 ) && (numberOfBlocksOn_1 > 0) ){
                    availableAction = 5;
                }
                else if( (currentPosition == 22 ) && (numberOfBlocksOn_2 > 0) ){
                    availableAction = 5;
                }
                else if( (currentPosition== 23 ) && (numberOfBlocksOn_3 > 0) ){
                    availableAction = 5;
                }
                else if( (currentPosition == 24 ) && (numberOfBlocksOn_4 > 0) ){
                    availableAction = 5;
                }
            }
            if(hasFlower){
                int currentPosition = previousColor;
                if( (currentPosition == 31 ) && (dropOffPlace_1 < block_size * 2) ){
                    availableAction = 6;
                }
                else if( (currentPosition == 32 ) && (dropOffPlace_2 < block_size * 2) ){
                    availableAction = 6;
                }
            } 
            return availableAction;
    }
    
    public void reset(){
                    numberOfBlocksOn_1 = block_size;
                    numberOfBlocksOn_2 = block_size;
                    numberOfBlocksOn_3 = block_size;
                    numberOfBlocksOn_4 = block_size;
                    dropOffPlace_1 = 0;
                    dropOffPlace_2 = 0;
                    maze[current_x][current_y] = previousColor;  
                    current_x = 1; current_y = 5;
                    previousColor = 0; 
                    maze[1][5] = 9; 
                    maze[1][1] = 21;maze[3][3] = 22;maze[4][1] = 23;maze[5][5] = 24;
                    maze[4][4] = 31;maze[5][1] = 32;
    }
            
    public void q_learning() throws FileNotFoundException, UnsupportedEncodingException {
        
        ReforcementLearning q = new ReforcementLearning();
        q.buildQTable();
        //q.print_table(1, learningMethod, policyType);
        setVisible(true);
        int runs = 1;
        int operation = 0;
        int carryingBlock = 0;
        int steps = 0;
        String bankAccountOutput = "";
        Boolean isFirstRun = true;
        Boolean isFirstDropOff = true;
        for(int i = 0; i < maxSteps; i++ ){
            steps++;
            int availableAction = 0;
            availableAction = check_action(); // return 5. pick up  6. drop off
            // get direction
            
            if(i == maxSteps / 2){
                q.print_table(i, learningMethod, policyType);
            }
            
            if(learningMethod == 1 && policyType == 2){            
                //if one dropp off location is full for experiment 2
                if( (dropOffPlace_1 == block_size * 2 ||  dropOffPlace_2 == block_size * 2) && isFirstDropOff){
                    q.print_table(i, learningMethod, policyType);
                isFirstDropOff = false;
                }
                 //when terminal state is reached for experiment 2
                if(runs == 2 && isFirstRun){
                    q.print_table(i, learningMethod, policyType);
                isFirstRun = false;
                } 
            }
            if(policyType == 1){
                operation = q.PRANDOM(current_x, current_y, availableAction, carryingBlock);
            }else if (policyType == 2){
                operation = q.PEPLOIT(current_x, current_y, availableAction, carryingBlock);
            }else if(policyType == 3){
                operation = q.PGREEDY(current_x, current_y, availableAction, carryingBlock);
            }

            //System.out.print(operation + " - " + availableAction + "\n" );
            if(operation >= 1 && operation <=4){
                q.learn(operation, current_x, current_y, carryingBlock); // 
                move(operation);
            }else{
                int actionType = action();
                
                q.learn(operation, current_x, current_y, carryingBlock); // 
                
                if(actionType == 5){ //pick up
                    carryingBlock = 1;
                }else if(actionType == 6){
                    carryingBlock = 0;
                }
                
                if(isPickUpEmpty){
                    q.checkPositions(current_x, current_y, 0);
                    isPickUpEmpty = false;
                }
                if(isDropOffFull){
                    q.checkPositions(current_x, current_y, 1);
                    isDropOffFull = false;
                }
                // restart
                if( dropOffPlace_1 == block_size * 2 &&  dropOffPlace_2 == block_size * 2){ //
                    //q.print_table(maxSteps, learningMethod, policyType);
                    if(q.bankAccount > 220){
                        q.print_table(q.bankAccount, learningMethod, policyType);
                    }
                    reset();
                    System.out.println("steps: " + steps); 
                    bankAccountOutput += "run#: " + String.format("%1$"+ 4+ "s", runs) + " --- steps: " + steps + " --- ";
                    steps = 0;
                    runs++;
                    System.out.println( "bank accounts: " + q.bankAccount );
                    bankAccountOutput += "bank accounts: " + q.bankAccount +  "\r\n";
                    q.bankAccount  = 0;
                    q.resetPositions();
                }
            }
                
            setTitle("PD WORLD - Steps: " + i);
            repaint();
            //System.out.println("i: " + i);
            try        
            {
                Thread.sleep(timeDelay);
            } 
            catch(InterruptedException ex) 
            {
                Thread.currentThread().interrupt();
            }
        }
        printBankAccount(bankAccountOutput);
        System.out.println("end"); 
        q.print_table(maxSteps, learningMethod, policyType);
        
    }
    
    public void SARSA() throws FileNotFoundException, UnsupportedEncodingException {
        
        ReforcementLearning q = new ReforcementLearning();
        setVisible(true);
        int runs = 1;
        int operation = 0;
        int previousOperation = 0;
        int currentState = 0;
        int nextState = 0;
        int carryingBlock = 0;
        int steps = 0;
        int availableAction = 0;
        String bankAccountOutput = "";
        if(policyType == 1){
              operation = q.PRANDOM(current_x, current_y, availableAction, carryingBlock);
        }else if (policyType == 2){
            operation = q.PEPLOIT(current_x, current_y, availableAction, carryingBlock);
        }else if(policyType == 3){
            operation = q.PGREEDY(current_x, current_y, availableAction, carryingBlock);

        }
        //System.out.println(operation + " --- " + current_x + ", " + current_y  + ", "+carryingBlock);
        currentState = current_x * 100 + current_y * 10 + carryingBlock;       
        move(operation);     
        previousOperation = operation;
        nextState = current_x * 100 + current_y * 10 + carryingBlock;
        for(int i = 0; i < maxSteps; i++ ){
            //if(i ==10 || i == 50 || i ==100 || i == 200)  q.print_table(i, learningMethod, policyType);
            steps++;
            availableAction = check_action(); // return 5. pick up  6. drop off
            if(policyType == 1){
                operation = q.PRANDOM(current_x, current_y, availableAction, carryingBlock);
            }else if (policyType == 2){
                operation = q.PEPLOIT(current_x, current_y, availableAction, carryingBlock);
            }else if(policyType == 3){
                operation = q.PGREEDY(current_x, current_y, availableAction, carryingBlock);
            }
            if(operation >= 1 && operation <=4){
                //System.out.println("current: " + currentState + " --- previous " + previousOperation  + "  next:  "+nextState +" --- operator: " + operation);
                q.learn_sarsa(currentState, nextState, previousOperation, operation); //
                currentState = current_x * 100 + current_y * 10 + carryingBlock;     
                previousOperation = operation;
                move(operation);
                nextState = current_x * 100 + current_y * 10 + carryingBlock;  
            }else{  
                q.learn_sarsa(currentState, nextState, previousOperation, operation); //   
                currentState = current_x * 100 + current_y * 10 + carryingBlock;
                previousOperation = operation; 
                int actionType = action();
                if(actionType == 5){ //pick up
                    carryingBlock = 1;
                }else if(actionType == 6){
                    carryingBlock = 0;
                }  
                nextState = current_x * 100 + current_y * 10 + carryingBlock;
                // restart after completion
                if( dropOffPlace_1 == block_size * 2 &&  dropOffPlace_2 == block_size * 2){ //
                    q.print_table(maxSteps, learningMethod, policyType);
                    if(q.bankAccount > 220) q.print_table(runs, learningMethod, policyType);
                    reset();
                    System.out.println("Steps: " + steps); 
                    bankAccountOutput += "run#: " + String.format("%1$"+ 4+ "s", runs) + " --- steps: " + steps + " --- ";
                    steps = 0;
                    runs++;
                    System.out.println( "bank accounts: " + q.bankAccount );
                    bankAccountOutput += "bank accounts: " + q.bankAccount +  "\r\n";
                    q.bankAccount  = 0;
                }
            }
            setTitle("PD WORLD - Steps: " + i + " --- Run: " + runs);
            repaint();
            //System.out.println("i: " + i);
            try        
            {
                Thread.sleep(timeDelay);
            } 
            catch(InterruptedException ex) 
            {
                Thread.currentThread().interrupt();
            }
            if(i == maxSteps / 2){
              q.print_table(i, learningMethod, policyType);
            }
        }
        printBankAccount(bankAccountOutput);
        System.out.println("end"); 
        q.print_table(maxSteps, learningMethod, policyType);
    }

    public void run() throws UnsupportedEncodingException, FileNotFoundException {
        
        if(learningMethod == 1){
            q_learning();
        }
        else if(learningMethod == 2){
            SARSA();
        } 
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        String s;
        PD pd = new PD();
        do {
            pd = new PD();
            pd.reset();
            input();
            pd.run(); 
            System.out.println("Enter 'terminate' to terminate or re-run with different parameters: " );
            s = sc.nextLine(); 
        } while (!(s.equals("terminate")||s.equals("Terminate") || s.equals("TERMINATE")) );    
      
        System.exit(0);
    }
}