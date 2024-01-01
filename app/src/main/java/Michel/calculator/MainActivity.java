package Michel.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Stack;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView result_view,solution_view;
    MaterialButton buttonC,buttonOpenBracket,buttonCloseBracket,buttonDivide,button9,
            button8,button7,button6,button5,button4,button3,button2,button1,button0,
            buttonMultiply,buttonSubtract,buttonAdd,buttonEqual,buttonAc,buttonPoint;

    String equation = "";
    ArrayList<String> history = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result_view = findViewById(R.id.result_view);
        solution_view = findViewById(R.id.solution_view);
        assignId(buttonC,R.id.button_c);
        assignId(buttonOpenBracket,R.id.button_open_bracket);
        assignId(buttonCloseBracket,R.id.button_close_bracket);
        assignId(buttonDivide,R.id.button_divide);
        assignId(button9,R.id.button_9);
        assignId(button8,R.id.button_8);
        assignId(button7,R.id.button_7);
        assignId(button6,R.id.button_6);
        assignId(button5,R.id.button_5);
        assignId(button4,R.id.button_4);
        assignId(button3,R.id.button_3);
        assignId(button2,R.id.button_2);
        assignId(button1,R.id.button_1);
        assignId(button0,R.id.button_0);
        assignId(buttonMultiply,R.id.button_multiply);
        assignId(buttonSubtract,R.id.button_subtract);
        assignId(buttonAdd,R.id.button_add);
        assignId(buttonAc,R.id.button_ac);
        assignId(buttonEqual,R.id.button_equal);
        assignId(buttonPoint,R.id.button_point);
    }
    void assignId(MaterialButton btn,int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        if (buttonText.equals(".") && equation.isEmpty()){
            buttonText = "0.";
        }
        if(buttonText.equals("C")){
            equation = equation.substring(0,equation.length()-1);
        }
        else if(buttonText.equals("ac")){
            equation = "";
            history.clear();
        }
        else if(buttonText.equals("=")){
            evaluate();
        }
        else{
            equation += buttonText;
        }
        solution_view.setText(equation);
    }

    public void evaluate(){
        Stack stack = new Stack();
        char[] eq = equation.toCharArray();
        String[] postfix = new String[eq.length];
        int j = 0;
        String number = "";
        char topStack = ' ';
        for(int i = 0; i < eq.length;i++){
            if(stack.empty() && !Character.isDigit(eq[i])){
                stack.push(eq[i]);
                topStack = (char) stack.peek();
                continue;
            }
            switch(eq[i]){
                case('('):stack.push(eq[i]);
                    break;
                case('+'):
                case('-'):
                    while(topStack != '*' && topStack != '/' && topStack != '('){
                        postfix[j] = String.valueOf(topStack);
                        j++;
                        stack.pop();
                        if(!stack.empty()) {
                            topStack = (char) stack.peek();
                        }
                        else break;
                    }
                    stack.push(eq[i]);
                    break;
                case('*'):
                case('/'):
                    while(topStack != '('){
                        postfix[j] = String.valueOf(topStack);
                        j++;
                        stack.pop();
                        if(!stack.empty()){
                            topStack = (char)stack.peek();
                        }
                        else break;
                    }
                    stack.push(eq[i]);
                break;
                case(')'):
                    while(topStack != '('){
                        postfix[j] = String.valueOf(topStack);
                        j++;
                        stack.pop();
                        topStack = (char)stack.peek();
                    }
                    break;
                default:
                    if(i < eq.length-1 && Character.isDigit(eq[i+1])){
                        number += eq[i];
                    }
                    else{
                        number += eq[i];
                        postfix[j] = number;
                        number = "";
                        j++;
                    }
                    break;
            }
        }
        while(!stack.empty()){
            if((char)stack.peek() != '(') {
                postfix[j] = String.valueOf((char)stack.peek());
                j++;
            }
            stack.pop();
        }
        for(int i = 0; i < j;i++){
            Log.d("postfix",postfix[i]);
        }
    }
    public String add(int a, int b){
        return Integer.toString(a+b);
    }
    
}