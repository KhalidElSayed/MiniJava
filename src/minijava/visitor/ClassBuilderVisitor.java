package minijava.visitor;

import junit.framework.Assert;
import minijava.ast.AST;
import minijava.ast.And;
import minijava.ast.ArrayAssign;
import minijava.ast.ArrayLength;
import minijava.ast.ArrayLookup;
import minijava.ast.Assign;
import minijava.ast.Block;
import minijava.ast.BooleanLiteral;
import minijava.ast.BooleanType;
import minijava.ast.Call;
import minijava.ast.ClassDecl;
import minijava.ast.IdentifierExp;
import minijava.ast.If;
import minijava.ast.IntArrayType;
import minijava.ast.IntegerLiteral;
import minijava.ast.IntegerType;
import minijava.ast.LessThan;
import minijava.ast.MainClass;
import minijava.ast.MethodDecl;
import minijava.ast.Minus;
import minijava.ast.NewArray;
import minijava.ast.NewObject;
import minijava.ast.NodeList;
import minijava.ast.Not;
import minijava.ast.ObjectType;
import minijava.ast.Plus;
import minijava.ast.Print;
import minijava.ast.Program;
import minijava.ast.This;
import minijava.ast.Times;
import minijava.ast.Type;
import minijava.ast.VarDecl;
import minijava.ast.While;
import minijava.typechecker.ErrorMessage;
import minijava.typechecker.ErrorReport;
import minijava.typechecker.TypeCheckerException;
import minijava.typechecker.implementation.ClassEntry;
import minijava.typechecker.implementation.MethodEntry;
import minijava.util.FunTable;
import minijava.util.ImpTable;
import minijava.util.ImpTable.DuplicateException;

public class ClassBuilderVisitor extends ReflectionVisitor {
	
	ImpTable<ClassEntry> classTable;
	ErrorReport reporter;
	
	public ClassBuilderVisitor() {
		classTable = new ImpTable<ClassEntry>();
		reporter = new ErrorReport();
	}

	public ImpTable<ClassEntry> visit(Program n) {
		visit(n.mainClass);
		visit(n.classes);
		return classTable;
	}
	
	public <T extends AST> void visit(NodeList<T> nodeList) {
		for (int i = 0; i < nodeList.size(); i++) {
			visit(nodeList.elementAt(i));
		}
	}
	
	public <T extends AST> void visit(NodeList<T> nodeList, ClassEntry entry) {
		for (int i = 0; i < nodeList.size(); i++) {
			visit(nodeList.elementAt(i), entry);
		}
	}
	
	public <T extends AST> void visit(NodeList<T> nodeList, MethodEntry entry) {
		for (int i = 0; i < nodeList.size(); i++) {
			visit(nodeList.elementAt(i), entry);
		}
	}

	public void visit(MainClass n) {
		ClassEntry mainClass = new ClassEntry();
		
		MethodEntry mainMethod = new MethodEntry();
		try {
			mainClass.getMethods().put("main", mainMethod);
		} catch (DuplicateException e2) {
			// this will never happen due to the parser grammar
		}
		
		try {
			mainMethod.getParameters().put(n.argName, new ObjectType("String"));
		} catch (DuplicateException e1) {
			// this will never happen due to the parser grammar
		}
		
		//visit(n.statement);
		
		try {
			classTable.put(n.className, mainClass);
		} catch (DuplicateException e) {
			reporter.duplicateDefinition(n.className);
		}
	}

	public void visit(ClassDecl n) {
		ClassEntry classEntry = new ClassEntry();
		
		try {
			classTable.put(n.name, classEntry);
		} catch (DuplicateException e) {
			reporter.duplicateDefinition(n.name);
		}
		
		visit(n.vars, classEntry);
		visit(n.methods, classEntry);
	}

	public void visit(VarDecl var, ClassEntry entry) {
		try {
			entry.getFields().put(var.name, var.type);
		} catch (DuplicateException e) {
			reporter.duplicateDefinition(var.name);
		}
	}
	
	public void visit(VarDecl var, MethodEntry entry) {
		try {
			entry.getParameters().put(var.name, var.type);
		} catch (DuplicateException e) {
			reporter.duplicateDefinition(var.name);
		}
	}

	public void visit(MethodDecl n, ClassEntry entry) {
		MethodEntry method = new MethodEntry();
		try {
			entry.getMethods().put(n.name, method);
		} catch (DuplicateException e) {
			reporter.duplicateDefinition(n.name);
		}
		
		visit(n.formals, method);
		visit(n.vars, method);
		visit(n.statements, method);
	}

	public void visit(IntArrayType n) {
	}

	public FunTable visit(BooleanType n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(IntegerType n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(ObjectType n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(Block n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(If n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(While n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(Print n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(Assign n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(And n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(LessThan n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(Plus n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(Minus n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(Times n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(ArrayLength n) {
		// TODO Auto-generated method stub
		return null;
	}

	public FunTable visit(Call n) {
		// TODO Auto-generated method stub
		return null;
	}

}
