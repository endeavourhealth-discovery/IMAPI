package org.endeavourhealth.imapi.model.visitor;

import org.endeavourhealth.imapi.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ObjectModelVisitor {
    public interface IExpressionSetVisitor { void visit(Set<ClassExpression> expressions); }
    public interface IExpressionListVisitor { void visit(List<ClassExpression> expressions); }
    public interface IConceptReferenceSetVisitor { void visit(Set<ConceptReference> conceptReferences); }
    public interface IConceptReferenceListVisitor { void visit(List<ConceptReference> conceptReferences); }
    public interface IExpressionVisitor { void visit(ClassExpression expression); }
    public interface IConceptReferenceVisitor { void visit(ConceptReference conceptReference); }
    public interface IPropertyValueVisitor { void visit(PropertyValue propertyValue); }
    public interface IDataPropertyValueVisitor { void visit(PropertyValue propertyValue); }
    public interface IAnnotationSetVisitor { void visit(Set<Annotation> annotations); }
    public interface IMemberListVisitor { void visit(List<ClassExpression> members); }

    public IExpressionSetVisitor SubClassVisitor = (expressions) -> {};
    public IExpressionSetVisitor EquivalentToVisitor = (expressions) -> {};
    public IConceptReferenceSetVisitor DisjointWithVisitor = (conceptReferences) -> {};
    public IExpressionVisitor ExpressionVisitor = (expression) -> {};
    public IExpressionVisitor ExpressionExitVisitor = (expression) -> {};
    public IConceptReferenceVisitor ClassVisitor = (conceptReference) -> {};
    public IExpressionListVisitor IntersectionVisitor = (expressions) -> {};
    public IExpressionListVisitor UnionVisitor = (expressions) -> {};
    public IExpressionVisitor ComplementOfVisitor = (expression) -> {};
    public IExpressionVisitor ComplementOfExitVisitor = (expression) -> {};
    public IPropertyValueVisitor PropertyValueVisitor = (propertyValue) -> {};
    public IPropertyValueVisitor PropertyValueExitVisitor = (propertyValue) -> {};
    public IDataPropertyValueVisitor DataPropertyValueVisitor = (dataPropertyValue) -> {};
    public IConceptReferenceListVisitor ObjectOneOfVisitor = (conceptReferences) -> {};
    public IAnnotationSetVisitor AnnotationsVisitor = (annotations) -> {};
    public IMemberListVisitor MemberListVisitor = (members) -> {};
    public IMemberListVisitor MemberListExitVisitor = (members) -> {};

    public void visit(Concept concept) {
        if (concept.getAnnotations() != null)
            this.AnnotationsVisitor.visit(concept.getAnnotations());

        if (concept.getSubClassOf() != null) {
            this.SubClassVisitor.visit(concept.getSubClassOf());
            visit(concept.getSubClassOf());
        }

        if (concept.getEquivalentTo() != null) {
            this.EquivalentToVisitor.visit(concept.getEquivalentTo());
            visit(concept.getEquivalentTo());
        }

        if (concept.getExpression() != null)
            this.visit(concept.getExpression());

        if (concept.getDisjointWith() != null)
            this.DisjointWithVisitor.visit(concept.getDisjointWith());

        if (concept.getMember() != null) {
            this.MemberListVisitor.visit(concept.getMember());
            visit(concept.getMember());
            this.MemberListExitVisitor.visit(concept.getMember());
        }
    }

    public void visit(Collection<ClassExpression> classExpressions) {
        classExpressions.forEach(this::visit);
    }

    public void visit(ClassExpression expression) {
        this.ExpressionVisitor.visit(expression);

        if (expression.getClazz() != null)
            this.ClassVisitor.visit(expression.getClazz());

        if (expression.getIntersection() != null) {
            this.IntersectionVisitor.visit(expression.getIntersection());
            visit(expression.getIntersection());
        }

        if (expression.getUnion() != null) {
            this.UnionVisitor.visit(expression.getUnion());
            visit(expression.getUnion());
        }

        if (expression.getComplementOf() != null) {
            this.ComplementOfVisitor.visit(expression.getComplementOf());
            this.visit(expression.getComplementOf());
            this.ComplementOfExitVisitor.visit(expression.getComplementOf());
        }

        if (expression.getPropertyValue() != null)
            this.visit(expression.getPropertyValue());

        if (expression.getPropertyValue() != null)
            this.visit(expression.getPropertyValue());

        if (expression.getObjectOneOf() != null)
            this.visit(expression.getObjectOneOf());

        if (expression.getAnnotations() !=  null)
            this.visit(expression.getAnnotations());

        this.ExpressionExitVisitor.visit(expression);
    }

    private void visit(PropertyValue propertyValue) {
        this.PropertyValueVisitor.visit(propertyValue);

        if (propertyValue.getExpression() != null)
            this.visit(propertyValue.getExpression());

        this.PropertyValueExitVisitor.visit(propertyValue);
    }

    private void visit(List<ConceptReference> objectOneOf) {
        this.ObjectOneOfVisitor.visit(objectOneOf);
    }

    private void visit(Set<Annotation> annotations) {
        this.AnnotationsVisitor.visit(annotations);
    }
}
