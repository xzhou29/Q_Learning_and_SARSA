/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rl;

import java.util.Map;

/**
 *
 * @author XinZhou
 */
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.TreeMap;

public class ReforcementLearning {
    private double Reward = 0;
    private double gamma ;
    private double alpha ;
    private int epsilon;
    private int randomSteps = 200;
    public int bankAccount = 0;
    // column: 1.up 2.down 3.right 4.left 5 pick up 6 drop off
    // value: 0 is applicable 9 is non-applicable
    Map<Integer, double[] > q_table = new TreeMap<Integer, double[] >(); // 
    public ReforcementLearning() {
        this.alpha = 0.3;
        this.gamma = 0.5;
        this.epsilon = 85; // %100
        this.bankAccount = 0;
    }
    
    public int PRANDOM(int current_x, int current_y, int availableAction, int carryingBlock){ 
        int currentState = current_x * 100 + current_y * 10 + carryingBlock;
        check_state(currentState);
        int nextDirection = 0; // 1.up 2.down 3.right 4.left 
        if(availableAction == 5 || availableAction == 6){
                nextDirection = availableAction ;
                return nextDirection;
        }
        int applicableOperationSize = 0;
        int applicableOperation[] = new int[4];
        for(int i = 1; i < 5; i++){
            if(check_applicable(i, current_x, current_y)){  
                applicableOperation[applicableOperationSize] = i;
                applicableOperationSize++;
            }
        }
        
        int r = (int )(Math.random() * applicableOperationSize + 1);
        nextDirection = applicableOperation[r - 1];
        randomSteps--;
        return nextDirection;
    }
    
    public int PEPLOIT(int current_x, int current_y, int availableAction, int carryingBlock){ 
        int currentState = current_x * 100 + current_y * 10 + carryingBlock;
        check_state(currentState);
        int nextDirection = 1; // 1.up 2.down 3.right 4.left
        
        if(availableAction == 5 || availableAction == 6){
                nextDirection = availableAction ;
                return nextDirection;
        }
        while( !check_applicable(nextDirection ,current_x, current_y)){ // to ensure intial value is applicable
            nextDirection++;  
        } 
        int random = (int )(Math.random() * 100 + 1);
         //if random < spsilon(0.9) pick highest value
        //System.out.println("i: " + randomSteps);
        if( random <= epsilon && randomSteps <= 0){
            double[] temp =  new double[7];
            temp = q_table.get(currentState);
            double best = -99.0;
            int applicableOperationSize = 1;
            int applicableOperation[] = new int[4];
            applicableOperation[0] =  nextDirection;
            for(int i = 1; i < 5; i++){
                //System.out.println("i: " + i);
                //System.out.println("position: " + current_x + "," +current_y);
                if(check_applicable(i,current_x, current_y)){
                    double current = temp[i];
                    if(current > best){
                            best = current;
                            nextDirection = i;
                        }
                    else if(current == best){
                        applicableOperation[applicableOperationSize] = i;
                        applicableOperationSize++;
                    }
                }
             
            }
            if(applicableOperationSize > 1){
                int r = (int )(Math.random() * applicableOperationSize + 1);
                nextDirection = applicableOperation[r - 1];
            }
            return nextDirection;
            // choose best
        }else{
            int applicableOperationSize = 0;
            int applicableOperation[] = new int[4];
            for(int i = 1; i < 5; i++){
                if(check_applicable(i, current_x, current_y)){  
                    applicableOperation[applicableOperationSize] = i;
                    applicableOperationSize++;
                }
            }
            int r = (int )(Math.random() * applicableOperationSize + 1);
            nextDirection = applicableOperation[r - 1];
            //System.out.println(nextDirection);
            if(randomSteps > -1){
                randomSteps--;
            }
            return nextDirection;
        }   
    }
   
    public int PGREEDY(int current_x, int current_y, int availableAction, int carryingBlock){ 
        
        int currentState = current_x * 100 + current_y * 10 + carryingBlock;
        check_state(currentState);
        int nextDirection = 1; // 1.up 2.down 3.right 4.left
        
        if(availableAction == 5 || availableAction == 6){
                nextDirection = availableAction ;
                return nextDirection;
        }
        while( !check_applicable(nextDirection ,current_x, current_y)){ // to ensure intial value is applicable
            nextDirection++;  
        } 
        
        double[] temp = q_table.get(currentState);
        double best = -99.00;
       // System.out.println(temp[nextDirection]);
        int applicableOperationSize = 1;
        int applicableOperation[] = new int[4];
        applicableOperation[0] =  nextDirection;
        for(int i = 1; i < 5; i++){
            if(check_applicable(i,current_x, current_y)){
                double current = temp[i];
                if(current > best){
                        //System.out.println(best);
                        best = current;
                        nextDirection = i;
                    }
                else if(current == best){
                    applicableOperation[applicableOperationSize] = i;
                    applicableOperationSize++;
                }
            }

        }
        if(applicableOperationSize > 1){
            int r = (int )(Math.random() * applicableOperationSize + 1);
            nextDirection = applicableOperation[r - 1];
        }
        return nextDirection;
    }

    public void learn(int operation, int current_x, int current_y, int carryingBlock){
        // q-learning: Q(a,s)  (1-alpha)*Q(a,s) + alpha*[Reward(s’,a,s)+ learningRate *maxa’Q(a’,s’)]
        // Sarsa: Q(a,s)  Q(a,s) +  alpha [ R(s) + gamma*Q(a’,s’) - Q(a,s)]
        int currentState = 0;
        int nextState = 0;
        double[] currentStateValue =  new double[7];
        double[] nextStateValue =  new double[7];
        currentState = current_x * 100 + current_y * 10 + carryingBlock; 
        //System.out.println( q_table.get(currentState)[operation]);
        switch (operation) {
            case 1:
                current_x -= 1;
                nextState = current_x * 100 + current_y * 10 + carryingBlock;
                Reward = -1;
                bankAccount-=1;
                break;
            case 2:
                current_x += 1;
                nextState = current_x * 100 + current_y * 10 + carryingBlock;
                Reward = -1;
                bankAccount-=1;
                break;
            case 3:
                current_y -= 1;
                nextState = current_x * 100 + current_y * 10 + carryingBlock;
                Reward = -1;
                bankAccount-=1;
                break;
            case 4:
                current_y += 1;
                nextState = current_x * 100 + current_y * 10 + carryingBlock;
                Reward = -1;
                bankAccount-=1;
                break;
            case 5:
                nextState = current_x * 100 + current_y * 10 + 1;
                Reward = 12;
                bankAccount+=12;
                break;
            case 6: 
                nextState = current_x * 100 + current_y * 10 ;
                Reward = 12;
                bankAccount+=12;
                break;
            default:
                break;
        }
        check_state(currentState);
        check_state(nextState);           
        currentStateValue = q_table.get(currentState);
        nextStateValue = q_table.get(nextState);
        double[] temp = nextStateValue; 
        double max = temp[2];    
        for(int i = 2; i < 7; i++){
            if(temp[i] > max) max = temp[i];
        }
        
        //max = nextStateValue[maxValueOperator];
        //System.out.println(max);
        //Q(a,s)  (1-alpha)*Q(a,s) + alpha*[Reward(s’,a,s)+ gamma *maxa’Q(a’,s’)]
        currentStateValue[operation] = currentStateValue[operation] + alpha * (Reward + gamma * max - currentStateValue[operation]);
    }
    
    public void learn_sarsa(int currentState, int nextState, int operation, int nextOperation){
        double[] currentStateValue =  new double[7];
        double[] nextStateValue =  new double[7];
        check_state(currentState);
        check_state(nextState); 
        currentStateValue = q_table.get(currentState);
        nextStateValue = q_table.get(nextState);
        double[] temp = nextStateValue;
        switch (operation) {
            case 1:
                 Reward = -1;
                bankAccount-=1;
                break;
            case 2:
                Reward = -1;
                bankAccount-=1;
                break;
            case 3:
                 Reward = -1;
                bankAccount-=1;
                break;
            case 4:
                Reward = -1;
                bankAccount-=1;
                break;
            case 5: 
                Reward = 12;
                bankAccount+=12;
                break;
            case 6: 
                Reward = 12;
                bankAccount+=12;
                break;
            default:
                break;
        }
        // Sarsa: Q(a,s) = Q(a,s) +  alpha [ R(s) + gamma*Q(a’,s’) - Q(a,s)]
        currentStateValue[operation] = currentStateValue[operation] + alpha * (Reward + gamma * nextStateValue[nextOperation] - currentStateValue[operation] );

    }
    
    public void check_state(int state){
        if(q_table.get(state) == null){
            //System.out.println("null");
            double[] temp =  new double[7];
            for(int i = 1; i < 7; i++){
                temp[i] = 0.00;
            }
            q_table.put(state, temp);
        }
    }
    
    public boolean check_applicable(int i, int current_x, int current_y){ // for direction
        if(i == 1 && current_x > 1){
            return true;
        }else if(i == 2 && current_x < 5){
            return true;
        }else if(i == 3 && current_y > 1){
            return true;
        }else if(i == 4 && current_y < 5){
            return true;
        }
        return false;
    }
    
    public void print_table(int steps, int learningMethod, int policyType) throws FileNotFoundException, UnsupportedEncodingException{
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
        PrintWriter writer0 = new PrintWriter(a + "_"+ b + "_onStep_" + steps + "_0.txt", "UTF-8");
        PrintWriter writer1 = new PrintWriter(a + "_"+ b + "_onStep_" + steps + "_1.txt", "UTF-8");
        Iterator it = q_table.entrySet().iterator();
        DecimalFormat df = new DecimalFormat("#.####");
        
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            double[] temp = q_table.get(pair.getKey());
            int qTableType = (Integer) pair.getKey();
           
            if(qTableType % 10 == 0){
                String value = "";
                value += pair.getKey() + " ";
                for( int i = 1; i < 7; i++ ){
                    if(temp[i] == -99.00){
                        value += String.format("%1$"+ 14 + "s",  "0");
                    }else{
                        value += String.format("%1$"+ 14 + "s", df.format(temp[i]));
                    }
                }
                writer0.println( value + "\n");
            }  
            else{
                String value = "";
                value += pair.getKey() + " ";
                for( int i = 1; i < 7; i++ ){
                    if(temp[i] == -99.00){
                        value += String.format("%1$"+ 14 + "s",  "0");
                    }else{
                        value += String.format("%1$"+ 14 + "s", df.format(temp[i])) ;
                    }
                }
                writer1.println( value + "\n");
            }
            
           
            //it.remove(); // avoids a ConcurrentModificationException
        }
      
        writer0.close();
        writer1.close();
    }
    
    public void buildQTable(){
        
        //<editor-fold defaultstate="collapsed" desc="0">
        for(int x = 1; x < 6; x++ ){
            for(int y = 1; y < 6; y++ ){
                int state_0 = x * 100 + y * 10 + 0;
                double[] temp =  new double[7];
                if(x == 1 && y == 1){
                    for(int i = 1; i < 7; i++){
                        if(i == 1 || i == 3 || i == 6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 3 && y == 3){
                    for(int i = 1; i < 7; i++){
                        if( i == 6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 4 && y == 1){
                    for(int i = 1; i < 7; i++){
                        if(i == 3 || i == 6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 4 && y == 4){
                    for(int i = 1; i < 7; i++){
                        if(i == 5 || i == 6 )  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 1 && y == 5){
                    for(int i = 1; i < 7; i++){
                        if(i == 1 || i == 4 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 5 && y == 1){
                    for(int i = 1; i < 7; i++){
                        if(i == 2 || i == 3 || i ==5 || i == 6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 5 && y == 5){
                    for(int i = 1; i < 7; i++){
                        if(i == 2 || i == 4 || i == 6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if( x == 1 ){
                    for(int i = 1; i < 7; i++){
                        if(i == 1 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if( x == 5 ){
                    for(int i = 1; i < 7; i++){
                        if(i == 2 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if( y == 1){
                    for(int i = 1; i < 7; i++){
                        if(i == 3 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if( y == 5){
                    for(int i = 1; i < 7; i++){
                        if(i == 4 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }else{
                    for(int i = 1; i < 7; i++){
                        if( i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                
                q_table.put(state_0, temp);
            }
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="1">
        for(int x = 1; x < 6; x++ ){
            for(int y = 1; y < 6; y++ ){
                int state_1 = x * 100 + y * 10 + 1;
                double[] temp =  new double[7];
                if(x == 1 && y == 1){
                    for(int i = 1; i < 7; i++){
                        if(i == 1 || i == 3 || i == 6 || i == 5)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 3 && y == 3){
                    for(int i = 1; i < 7; i++){
                        if( i == 6 || i == 5 )  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 4 && y == 1){
                    for(int i = 1; i < 7; i++){
                        if(i == 3 || i == 6 || i == 5)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 4 && y == 4){
                    for(int i = 1; i < 7; i++){
                        if(i == 5 )  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 1 && y == 5){
                    for(int i = 1; i < 7; i++){
                        if(i == 1 || i == 4 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 5 && y == 1){
                    for(int i = 1; i < 7; i++){
                        if(i == 2 || i == 3 || i ==5)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if(x == 5 && y == 5){
                    for(int i = 1; i < 7; i++){
                        if(i == 2 || i == 4 || i == 6 || i == 5)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if( x == 1 ){
                    for(int i = 1; i < 7; i++){
                        if(i == 1 || i == 5 || i ==6 )  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if( x == 5 ){
                    for(int i = 1; i < 7; i++){
                        if(i == 2 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if( y == 1){
                    for(int i = 1; i < 7; i++){
                        if(i == 3 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                else if( y == 5){
                    for(int i = 1; i < 7; i++){
                        if(i == 4 || i ==5 || i ==6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }else{
                    for(int i = 1; i < 7; i++){
                        if( i == 5 || i == 6)  temp[i] = -999.00;
                        else temp[i] = 0.00;
                    }
                }
                q_table.put(state_1, temp);
            }
        }
//</editor-fold>
    }
    
    public void checkPositions(int x, int y, int isEmptyOrFull){ // empty = 0  full = 1
        // pick up postions: [1][1]  [3][3]  [4][1]  [5][5] // drop off positions: [4][4]  [5][1]
        if(isEmptyOrFull == 0){
            int state = x *100 + y *10 + 0;
            q_table.get(state)[0] = q_table.get(state)[5];
            q_table.get(state)[5] = -999.0;
        }else if(isEmptyOrFull == 1){
            int state = x * 100 + y *10 + 1;
            q_table.get(state)[0] = q_table.get(state)[6];
            q_table.get(state)[6] = -999.0;
        }
    }
    
    public void resetPositions(){
        // pick up postions: [1][1]  [3][3]  [4][1]  [5][5] // drop off positions: [4][4]  [5][1]
//        q_table.get(110)[5] = 0.0;
//        q_table.get(330)[5] = 0.0;
//        q_table.get(410)[5] = 0.0;
//        q_table.get(550)[5] = 0.0;
//        q_table.get(441)[6] = 0.0;
//        q_table.get(511)[6] = 0.0;
        q_table.get(110)[5] = q_table.get(110)[0];
        q_table.get(330)[5] = q_table.get(330)[0];
        q_table.get(410)[5] = q_table.get(410)[0];
        q_table.get(550)[5] = q_table.get(550)[0];
        q_table.get(441)[6] = q_table.get(441)[0];
        q_table.get(511)[6] = q_table.get(511)[0];
    }
}
