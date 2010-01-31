package minijava.typechecker.implementation;

import minijava.ast.Program;
import minijava.typechecker.ErrorReport;
import minijava.typechecker.TypeChecked;
import minijava.typechecker.TypeCheckerException;
import minijava.util.FunTable;
import minijava.util.ImpTable;
import minijava.visitor.ClassBuilderVisitor;

public class TypeCheckerImplementation {
	
	Program program;
	ImpTable<ClassEntry> classTable;
	ErrorReport reporter;
	public TypeCheckerImplementation(Program program) {
		this.program = program;
	}

	public TypeChecked typeCheck() throws TypeCheckerException{
		reporter = new ErrorReport();
		buildClassTable();
		
		reporter.close();//this will throw the first exception that was reported, if it exists.
		return null;
	}

	public Object buildClassTable(){
		ClassBuilderVisitor builder = new ClassBuilderVisitor(reporter);
		classTable = builder.visit(program);
		return classTable;
	}

}
