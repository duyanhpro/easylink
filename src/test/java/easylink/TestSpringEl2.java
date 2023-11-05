package easylink;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class TestSpringEl2 {
    public static void main(String args[]) {
        System.out.println("Hi");

        String expression = "hello";
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);		// will output r.description
        EvaluationContext context = new StandardEvaluationContext();

        System.out.println(exp.getValue(context));		// output r.getDescription()
    }
}
