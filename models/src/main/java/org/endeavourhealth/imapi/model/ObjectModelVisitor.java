package org.endeavourhealth.imapi.model;

import java.util.List;
import java.util.Set;

public class ObjectModelVisitor {
    public interface IExpressionSetVisitor { void visit(Set<ClassExpression> expressions); }
    public interface IExpressionListVisitor { void visit(List<ClassExpression> expressions); }
    public interface IConceptReferenceSetVisitor { void visit(Set<ConceptReference> conceptReferences); }
    public interface IConceptReferenceListVisitor { void visit(List<ConceptReference> conceptReferences); }
    public interface IExpressionVisitor { void visit(ClassExpression expression); }
    public interface IConceptReferenceVisitor { void visit(ConceptReference conceptReference); }
    public interface IObjectPropertyValueVisitor { void visit(ObjectPropertyValue objectPropertyValue); }
    public interface IDataPropertyValueVisitor { void visit(DataPropertyValue dataPropertyValue); }
    public interface IAnnotationSetVisitor { void visit(Set<Annotation> annotations); }

    public IExpressionSetVisitor SubClassVisitor = (expressions) -> {};
    public IExpressionSetVisitor EquivalentToVisitor = (expressions) -> {};
    public IConceptReferenceSetVisitor DisjointWithVisitor = (conceptReferences) -> {};
    public IExpressionVisitor ExpressionVisitor = (expression) -> {};
    public IConceptReferenceVisitor ClassVisitor = (conceptReference) -> {};
    public IExpressionListVisitor IntersectionVisitor = (expressions) -> {};
    public IExpressionListVisitor UnionVisitor = (expressions) -> {};
    public IExpressionVisitor ComplementOfVisitor = (expression) -> {};
    public IObjectPropertyValueVisitor ObjectPropertyValueVisitor = (objectPropertyValue) -> {};
    public IDataPropertyValueVisitor DataPropertyValueVisitor = (dataPropertyValue) -> {};
    public IConceptReferenceListVisitor ObjectOneOfVisitor = (conceptReferences) -> {};
    public IAnnotationSetVisitor AnnotationsVisitor = (annotations) -> {};

    public void visit(Concept concept) {
        if (concept.getAnnotations() != null)
            this.AnnotationsVisitor.visit(concept.getAnnotations());

        if (concept.getSubClassOf() != null) {
            this.SubClassVisitor.visit(concept.getSubClassOf());
            concept.getSubClassOf().forEach(this::visitExpression);
        }

        if (concept.getEquivalentTo() != null) {
            this.EquivalentToVisitor.visit(concept.getEquivalentTo());
            concept.getEquivalentTo().forEach(this::visitExpression);
        }

        if (concept.getExpression() != null)
            this.visitExpression(concept.getExpression());

        if (concept.getDisjointWith() != null)
            this.DisjointWithVisitor.visit(concept.getDisjointWith());

    }

    private void visitExpression(ClassExpression expression) {
        this.ExpressionVisitor.visit(expression);

        if (expression.getClazz() != null)
            this.ClassVisitor.visit(expression.getClazz());

        if (expression.getIntersection() != null) {
            this.IntersectionVisitor.visit(expression.getIntersection());
            expression.getIntersection().forEach(this::visitExpression);
        }

        if (expression.getUnion() != null) {
            this.UnionVisitor.visit(expression.getUnion());
            expression.getUnion().forEach(this::visitExpression);
        }

        if (expression.getComplementOf() != null) {
            this.ComplementOfVisitor.visit(expression.getComplementOf());
            this.visitExpression(expression.getComplementOf());
        }

        if (expression.getObjectPropertyValue() != null)
            this.visitObjectPropertyValue(expression.getObjectPropertyValue());

        if (expression.getDataPropertyValue() != null)
            this.visitDataPropertyValue(expression.getDataPropertyValue());

        if (expression.getObjectOneOf() != null)
            this.visitObjectOneOf(expression.getObjectOneOf());

        if (expression.getAnnotations() !=  null)
            this.visitAnnotations(expression.getAnnotations());
    }

    private void visitObjectPropertyValue(ObjectPropertyValue objectPropertyValue) {
        this.ObjectPropertyValueVisitor.visit(objectPropertyValue);

        if (objectPropertyValue.getExpression() != null)
            this.visitExpression(objectPropertyValue.getExpression());
    }

    private void visitDataPropertyValue(DataPropertyValue dataPropertyValue) {
        this.DataPropertyValueVisitor.visit(dataPropertyValue);
    }

    private void visitObjectOneOf(List<ConceptReference> objectOneOf) {
        this.ObjectOneOfVisitor.visit(objectOneOf);
    }

    private void visitAnnotations(Set<Annotation> annotations) {
        this.AnnotationsVisitor.visit(annotations);
    }
}
