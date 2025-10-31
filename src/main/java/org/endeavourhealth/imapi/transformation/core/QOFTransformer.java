package org.endeavourhealth.imapi.transformation.core;

import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.qof.QOFDocument;

/**
 * Base interface for transforming QOF components to IMQ equivalents.
 * All transformation components should implement this interface to ensure
 * consistent transformation behavior and error handling.
 *
 * @param <T> The QOF source type to be transformed
 */
public interface QOFTransformer<T> {

  /**
   * Transforms a QOF component to IMQ format.
   *
   * @param source The QOF component to transform
   * @param context The transformation context containing state and configuration
   * @return The transformed IMQ component
   * @throws TransformationException if transformation fails
   */
  void transform(T source, TransformationContext context) throws TransformationException;

  /**
   * Validates that a QOF component is suitable for transformation.
   *
   * @param source The component to validate
   * @return true if the component is valid for transformation
   */
  boolean canTransform(T source);
}