package Michel.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView equation_view,solution_view,equation1,equation2,equation3,equation4,equation5;

    Dialog historyDialog;
    Button clearHistory;
    String equation = "";
    ArrayList<String> history = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyDialog = new Dialog(this);
        historyDialog.setContentView(R.layout.hisory_window);
        setContentView(R.layout.activity_main);
        solution_view = findViewById(R.id.solution_view);
        equation_view = findViewById(R.id.equation_view);
        assignId(R.id.button_c);
        assignId(R.id.button_open_bracket);
        assignId(R.id.button_close_bracket);
        assignId(R.id.button_divide);
        assignId(R.id.button_9);
        assignId(R.id.button_8);
        assignId(R.id.button_7);
        assignId(R.id.button_6);
        assignId(R.id.button_5);
        assignId(R.id.button_4);
        assignId(R.id.button_3);
        assignId(R.id.button_2);
        assignId(R.id.button_1);
        assignId(R.id.button_0);
        assignId(R.id.button_multiply);
        assignId(R.id.button_subtract);
        assignId(R.id.button_add);
        assignId(R.id.button_sign);
        assignId(R.id.button_equal);
        assignId(R.id.button_point);
        assignImageId(R.id.history);
        assignImageId(R.id.back_space);
        assignTextViewId(R.id.first_equation);
        assignTextViewId(R.id.second_equation);
        assignTextViewId(R.id.third_equation);
        assignTextViewId(R.id.fourth_equation);
        assignTextViewId(R.id.fifth_equation);
        clearHistory = historyDialog.findViewById(R.id.clear_history);
        clearHistory.setOnClickListener(this);
        Window window = historyDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.TOP | Gravity.START;
        params.y = 700;
    }
    void assignTextViewId(int id){
        TextView txt = historyDialog.findViewById(id);
        txt.setOnClickListener(this);
    }
    void assignImageId(int id){
        ImageButton btn = findViewById(id);
        btn.setOnClickListener(this);
    }
    void assignId(int id){
        MaterialButton btn = findViewById(id);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        if(view instanceof MaterialButton) {
            handleMaterialButtonClick((MaterialButton)view);
        }
        else if(view instanceof ImageButton){
            handleImageButtonClick((ImageButton) view);
        }
        else if(view instanceof TextView){
            handleTextViewClick((TextView) view);
        }
    }
    private void handleTextViewClick(TextView txt){
        equation = txt.getText().toString();
        equation_view.setText(equation);
        solution_view.setText(evaluate());
    }

    /*handles user input in handleMaterialButtonClick and handleImageButtonClick making sure the input
    * is a properly formatted equation*/
    private void handleMaterialButtonClick(MaterialButton button){
        String buttonText = button.getText().toString();

        char prev = ' ';
        char antepen = ' ';
        if (!equation.isEmpty()){
            prev = equation.charAt(equation.length()-1);
        }
        if(equation.length()-2 >= 0){
            antepen = equation.charAt(equation.length()-2);
        }
        /*prevents the first character being an operator*/
        if(equation.isEmpty() && !buttonText.equals("+/-") && isOperator(buttonText.charAt(0))){
            equation = "";
        }
        /*prevents the user adding an operator after an open parenthesis*/
        else if(!buttonText.equals("+/-") && isOperator(buttonText.charAt(0)) && prev == '('){
            equation = equation;
        }
        /*prevents user from placing two operators in a row*/
        else if(!buttonText.equals("+/-") && isOperator(prev) && isOperator(buttonText.charAt(0))){
            if(antepen != '(') {
                equation = equation.substring(0, equation.length() - 1);
                equation += buttonText;
            }
        }
        /*handles decimal point when the equation is empty*/
        else if (buttonText.equals(".") && equation.isEmpty()){
            equation += "0.";
        }
        /*handles the clear button*/
        else if(buttonText.equals("C")){
            equation = "";
            solution_view.setText("");
        }
        else if(buttonText.equals("=")){
            if(!equation.isEmpty()) {
                addHistory(equation);
                equation = solution_view.getText().toString();
                equation_view.setText(equation);
                solution_view.setText("");
            }
        }
        else if(buttonText.equals("Clear")){
            history.clear();
            setClearHistory();
        }
        else if(buttonText.equals("+/-")){
            StringBuilder equationBuilder = new StringBuilder(equation);
            /*inserts a negative sign and a parenthesis before any preceding numbers*/
            if(Character.isDigit(prev) || prev == '.'){
                int i = equation.length() -1;
                while(Character.isDigit(equation.charAt(i)) || equation.charAt(i) == '.'){
                    i--;
                }
                equationBuilder.insert(i+1, "(-");
            }
            /*remove negative sign if two are pressed in a row*/
            else if(prev == '-' && antepen == '('){
                equationBuilder.delete(equationBuilder.length()-2,equationBuilder.length());
            }
            else{
                equationBuilder.append("(-");
            }
            equation = equationBuilder.toString();
        }
        else if(buttonText.equals("(")){
            if(!equation.isEmpty() && !isOperator(prev)){
                equation += "*(";
            }
            else{
                equation+= "(";
            }
        }
        else if(buttonText.equals(")")){
            if(equation.contains("(") && prev != '(' && !isOperator(prev)){
                equation += ')';
            }
        }
        else{
            if (!equation.isEmpty() && equation.charAt(equation.length()-1) == ')' && !isOperator(buttonText.charAt(0))){
                equation += '*';
            }
            equation += buttonText;
            if(!isOperator(equation.charAt(equation.length()-1)) && containsOperator()){
                solution_view.setText(evaluate());
            }
            else{
                solution_view.setText("");
            }
        }
        equation_view.setText(equation);
    }
    private void handleImageButtonClick(ImageButton imagebutton){
        int id = imagebutton.getId();
        if(id == R.id.back_space && !equation.isEmpty()){
            equation = equation.substring(0,equation.length()-1);
            equation_view.setText(equation);
            if(!equation.isEmpty() && !isOperator(equation.charAt(equation.length()-1))){
                solution_view.setText(evaluate());
            }
            else{
                solution_view.setText("");
            }
        }
        if(id == R.id.history){
            equation1 = historyDialog.findViewById(R.id.first_equation);
            equation2 = historyDialog.findViewById(R.id.second_equation);
            equation3 = historyDialog.findViewById(R.id.third_equation);
            equation4 = historyDialog.findViewById(R.id.fourth_equation);
            equation5 = historyDialog.findViewById(R.id.fifth_equation);
            clearHistory = historyDialog.findViewById(R.id.clear_history);
            int len = history.size();
            switch(len){
                case 1: equation1.setText(history.get(0));
                    break;
                case 2: equation2.setText(history.get(0));
                    equation1.setText(history.get(1));
                    break;
                case 3: equation3.setText(history.get(0));
                    equation2.setText(history.get(1));
                    equation1.setText(history.get(2));
                    break;
                case 4: equation4.setText(history.get(0));
                    equation3.setText(history.get(1));
                    equation2.setText(history.get(2));
                    equation1.setText(history.get(3));
                    break;
                case 5: equation5.setText(history.get(0));
                    equation4.setText(history.get(1));
                    equation3.setText(history.get(2));
                    equation2.setText(history.get(3));
                    equation1.setText(history.get(4));
                    break;
                default:
                    setClearHistory();
                    break;
            }
            historyDialog.show();
        }
    }
    /* setClearHistory removes any stored equations the user may have input*/
    public void setClearHistory(){
        equation5.setText("");
        equation4.setText("");
        equation3.setText("");
        equation2.setText("");
        equation1.setText("");
    }
    /* toPostfix converts the string equation input by user to a postfix equation for easier
    *  processing and correct order of operations*/
    public ArrayList<String> toPostfix(){
        Stack<String> stack = new Stack<>();
        char[] eq = equation.toCharArray();
        ArrayList<String> postfix = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        for(int i = 0; i < eq.length;i++){
            char element = eq[i];
            switch(element){
                case '(':
                    stack.push(String.valueOf(element));
                    break;
                case ')':
                    while(!stack.peek().equals("(")){
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                    break;
                case '-':
                    /*replaces a '-' which in this case is representing a negative number to
                    * distinguish negative numbers from the minus sign, with '~'. Negative numbers
                    * always have the open parenthesis before them which isn't accepted for operators*/
                    if (eq[i-1] == '('){
                        number.append('~');
                        i++;
                        while(i < eq.length && (Character.isDigit(eq[i]) || eq[i] == '.')){
                            number.append(eq[i]);
                            i++;
                        }
                        i--;
                        postfix.add(number.toString());
                        number = new StringBuilder();
                        break;
                    }
                case '/':
                case '+':
                case '*':
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(String.valueOf(element))) {
                        postfix.add(stack.pop());
                    }
                    stack.push(String.valueOf(element));


                    break;
                default:
                    while(i < eq.length && (Character.isDigit(eq[i]) || eq[i] == '.')){
                        number.append(eq[i]);
                        i++;
                    }
                    i--;
                    postfix.add(number.toString());
                    number = new StringBuilder();
                    break;
            }
        }
        while(!stack.empty()){
            String top = stack.pop();
            if(!top.equals("(")) {
                postfix.add(top);
            }
        }
        return postfix;
    }
    /*evaluate evaluates*/
    public String evaluate(){
        ArrayList<String> postfix = toPostfix();
        if(postfix == null){
            return "";
        }
        Stack<String> stack = new Stack<>();
        for(int i = 0; i < postfix.size();i++){
            String term = postfix.get(i);
            if(!isOperator(term.charAt(0))){
                /*convert the negative number, indicated by the ~, back to a minus sign*/
                if(term.charAt(0) == '~') {
                    StringBuilder string = new StringBuilder(term);
                    string.replace(0,1,"-");
                    term = string.toString();
                }
                stack.push(term);
            }
            else{
                double num2 = Double.parseDouble(stack.pop());
                double num1 = Double.parseDouble(stack.pop());
                switch (term) {
                    case "+":
                        stack.push(addition(num1, num2));
                        break;
                    case "-":
                            stack.push(subtract(num1, num2));
                        break;
                    case "*":
                        stack.push(multiply(num1, num2));
                        break;
                    case "/":
                        if (num2 == 0) {
                            return "Error";
                        }
                        stack.push(divide(num1, num2));
                        break;
                }
            }
        }
        return stack.pop();
    }

    /*helps in some input checking*/
    public Boolean isOperator(char last){
        return last == '+' || last == '-' || last == '*' || last == '/';
    }
    public String addition(double a, double b){
        return formatResult(a+b);
    }
    public String subtract(double a, double b){
        return formatResult(a-b);
    }
    public String multiply(double a, double b){
        return formatResult(a*b);
    }
    public String divide(double a, double b){
        return formatResult(a/b);
    }
    /*format the output to avoid some rounding errors and to avoid text overflow on the GUI*/
    public String formatResult(double value){
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
    /* Handle the saving of*/
    private void addHistory(String equation){
        if(history.size() == 5){
            history.remove(4);
        }
        history.add(0,equation);
    }
    /*precedence helps determine order of operations*/
    private int precedence(String operator){
        switch(operator){
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }
    /*containsOperator helps in determining if the input needs evaluation*/
    private Boolean containsOperator(){
        for(int i = 0; i < equation.length();i++){
            if(isOperator(equation.charAt(i))){
                return true;
            }
        }
        return false;
    }
}