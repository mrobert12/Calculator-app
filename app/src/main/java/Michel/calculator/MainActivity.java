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

import java.util.ArrayList;
import java.util.Stack;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView equation_view,solution_view,equation1,equation2,equation3,equation4,equation5;
    MaterialButton buttonC,buttonOpenBracket,buttonCloseBracket,buttonDivide,button9,
            button8,button7,button6,button5,button4,button3,button2,button1,button0,
            buttonMultiply,buttonSubtract,buttonAdd,buttonEqual,buttonSign,buttonPoint;
    ImageButton backSpace,buttonHistory;
    Dialog historyDialog;
    Button clearHistory;
    String equation = "";
    ArrayList<String> history = new ArrayList<>();
    private View mainLayout;
    private View historyLayout;


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
    private void handleMaterialButtonClick(MaterialButton button){
        String buttonText = button.getText().toString();

        if (buttonText.equals(".") && equation.isEmpty()){
            buttonText = "0.";
            equation += buttonText;
        }
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
            if(!equation.isEmpty()){
                char first = equation.charAt(0);
                if(first == '-'){
                    equation = equation.substring(1);
                }
                else{
                    equation = '-' + equation;
                }
            }
            else{
                equation = '-' + equation;
            }
        }
        else if(buttonText.equals("(")){
            if(!equation.isEmpty() && !isOperator(equation.charAt(equation.length()-1))){
                equation += "*(";
            }
            else{
                equation+= "(";
            }
        }
        else if(buttonText.equals(")")){
            if(equation.contains("(") && equation.charAt(equation.length()-1) != '('){
                equation += ')';
            }
        }
        else{
            if (!equation.isEmpty() && equation.charAt(equation.length()-1) == ')' && !isOperator(buttonText.charAt(0))){
                equation += '*';
            }
            equation += buttonText;
            if(!equation.isEmpty() && !isOperator(equation.charAt(equation.length()-1)) && containsOperator()){
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
                    equation4.setText(history.get(0));
                    equation3.setText(history.get(1));
                    equation2.setText(history.get(2));
                    equation1.setText(history.get(3));
                    break;
                default:
                    setClearHistory();
                    break;
            }
            historyDialog.show();
        }
    }
    public void setClearHistory(){
        equation5.setText("");
        equation4.setText("");
        equation3.setText("");
        equation2.setText("");
        equation1.setText("");
    }

    public ArrayList<String> toPostfix(){
        Stack<String> stack = new Stack<String>();
        char[] eq = equation.toCharArray();
        ArrayList<String> postfix = new ArrayList<String>();
        String number = "";
        char topStack = ' ';
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
                case '*':
                case '/':
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/"))) {
                        postfix.add(stack.pop());
                    }
                    stack.push(String.valueOf(element));
                    break;
                case '+':
                case '-':
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                    }
                    stack.push(String.valueOf(element));
                    break;
                default:
                    while(i < eq.length && (Character.isDigit(eq[i]) || eq[i] == '.')){
                        number += eq[i];
                        i++;
                    }
                    i--;
                    postfix.add(number);
                    number = "";
                    break;
            }
        }
        while(!stack.empty()){
            String top = stack.pop();
            if(!top.equals("(")) {
                postfix.add(top);
            }
        }
        for(int i = 0;i < postfix.size();i++){
            Log.d("Postfix",postfix.get(i));
        }
        return postfix;
    }
    public String evaluate(){
        ArrayList<String> postfix = toPostfix();
        if(postfix == null){
            return "";
        }
        Stack<String> stack = new Stack<String>();
        for(int i = 0; i < postfix.size();i++){
            if(!isOperator(postfix.get(i).charAt(0))){
                stack.push(postfix.get(i));
            }
            else{
                double num2 = Double.parseDouble(stack.pop());
                double num1 = Double.parseDouble(stack.pop());
                switch (postfix.get(i)) {
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

        String result = stack.pop();
        double numResult = Double.parseDouble((result));
        if(numResult % 1 == 0){
            result = Integer.toString((int)numResult);
        }
        return result;
    }

    public Boolean isOperator(char last){
        return last == '+' || last == '-' || last == '*' || last == '/';
    }
    public String addition(double a, double b){
        return Double.toString(a+b);
    }
    public String subtract(double a, double b){
        return Double.toString(a-b);
    }
    public String multiply(double a, double b){
        return Double.toString(a*b);
    }
    public String divide(double a, double b){
        return Double.toString(a/b);
    }
    private void addHistory(String equation){
        if(history.size() == 5){
            history.remove(4);
        }
        history.add(0,equation);
    }

    private Boolean containsOperator(){
        for(int i = 0; i < equation.length();i++){
            if(isOperator(equation.charAt(i))){
                return true;
            }
        }
        return false;
    }
}