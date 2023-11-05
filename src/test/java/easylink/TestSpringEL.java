package easylink;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import easylink.entity.Role;

public class TestSpringEL {

	public static void main(String[] args) {
		
		Role r = new Role();
		r.setId(1);
		r.setName("role 1");
		r.setStatus(Role.STATUS_ACTIVE);
		r.setDescription("super role for test");

		Role r2 = new Role();
		r2.setId(2);
		r2.setName("role 2");
		r2.setStatus(Role.STATUS_ACTIVE);
		r2.setDescription("small role");
		
		Role [] roles = new Role[] {r, r2};

		String expression = "#r.description";
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(expression);		// will output r.description
		EvaluationContext context = new StandardEvaluationContext(r);
		
		
		System.out.println(exp.getValue(context));		// output r.getDescription()
		
		exp = parser.parseExpression("#roles[1].getDescription() + 'abc'");		// will output r.description
		context = new StandardEvaluationContext(r);
		context.setVariable("roles", roles);
		context.setVariable("role2", roles);
		System.out.println(exp.getValue(context));
	}
	
	
	

}
