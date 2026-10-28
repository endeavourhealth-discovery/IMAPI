package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SetModelServiceTest {

  private static final String ROOT = "http://endhealth.info/im#Root";
  private static final String A = "http://endhealth.info/im#A";
  private static final String B = "http://endhealth.info/im#B";
  private static final String C = "http://endhealth.info/im#C";
  @Mock
  SetRepository setRepository;
  SetService setService;

  private static Concept concept(String iri, String name) {
    return new Concept().setIri(iri).setName(name);
  }

  private static Set<Concept> setOf(Concept... concepts) {
    return new LinkedHashSet<>(List.of(concepts));
  }

  private static Set<String> containerIris(Concept concept) {
    if (concept.getIsContainedIn() == null) return Set.of();
    return concept.getIsContainedIn().stream().map(TTEntity::getIri).collect(Collectors.toSet());
  }

  private static Concept find(Set<Concept> concepts, String iri) {
    return concepts.stream().filter(c -> iri.equals(c.getIri())).findFirst().orElseThrow();
  }

  @BeforeEach
  void initMocks() {
    setService = new SetService(setRepository);
  }

  @Test
  void getSetExport_NullIri() {
    SetOptions setOptions = new SetOptions(null, false, true, true, true, List.of(), List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getSetExport_EmptyIri() {
    SetOptions setOptions = new SetOptions("", false, true, true, true, List.of(), List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getSetExport_EmptyFormat() {
    SetOptions setOptions = new SetOptions("", false, true, true, true, List.of(), List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getDistillation() {
    List<TTIriRef> conceptList = new ArrayList<>();
    conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#73211009"));
    conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#46635009"));
    conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#44054006"));
    conceptList.add(new TTIriRef().setIri("http://endhealth.info/im#Q_RegisteredGMS"));

    String iris = "<http://snomed.info/sct#73211009> <http://snomed.info/sct#46635009> <http://snomed.info/sct#44054006> <http://endhealth.info/im#Q_RegisteredGMS>";

    when(setRepository.getDistillation(iris)).thenReturn(Set.of("http://snomed.info/sct#46635009", "http://snomed.info/sct#44054006"));

    Set<String> distList = new HashSet<>();
    distList.add("http://snomed.info/sct#73211009");
    distList.add("http://endhealth.info/im#Q_RegisteredGMS");

    List<TTIriRef> actual = setService.getDistillation(conceptList);

    assertEquals(actual, distList.stream().map(distIri -> new TTIriRef().setIri(distIri)).toList());

  }

  @Test
  void getExpandedSetMembers_nestedAndOverlappingSubsets_usesFixedNumberOfQueries() throws Exception {
    // Root has subsets A and B; C is a subset of both A and B (a diamond).
    when(setRepository.getSubsetEdges(ROOT)).thenReturn(List.of(
      new SetRepository.SubsetEdge(A, "A", ROOT),
      new SetRepository.SubsetEdge(B, "B", ROOT),
      new SetRepository.SubsetEdge(C, "C", A),
      new SetRepository.SubsetEdge(C, "C", B)));
    when(setRepository.getExpansionFromIris(anyCollection(), eq(false), anyList(), anyList())).thenReturn(Map.of(
      ROOT, setOf(concept("urn:r1", "r1")),
      A, setOf(concept("urn:a1", "a1")),
      B, setOf(concept("urn:b1", "b1")),
      C, setOf(concept("urn:c1", "c1"))));

    Set<Concept> result = setService.getExpandedSetMembers(ROOT, true, false, true, List.of(), List.of());

    assertEquals(4, result.size());
    assertEquals(Set.of(), containerIris(find(result, "urn:r1")));
    assertEquals(Set.of(A), containerIris(find(result, "urn:a1")));
    assertEquals(Set.of(B), containerIris(find(result, "urn:b1")));
    assertEquals(Set.of(A, B, C), containerIris(find(result, "urn:c1")));

    @SuppressWarnings("unchecked")
    ArgumentCaptor<Collection<String>> expanded = ArgumentCaptor.forClass(Collection.class);
    verify(setRepository, times(1)).getExpansionFromIris(expanded.capture(), eq(false), anyList(), anyList());
    assertEquals(Set.of(ROOT, A, B, C), new HashSet<>(expanded.getValue()));
    assertEquals(4, expanded.getValue().size(), "each set should be expanded once");
    verify(setRepository, times(1)).getSubsetEdges(anyString());
    verify(setRepository, never()).getSubsetIrisWithNames(anyString());
    verify(setRepository, never()).getExpansionFromIri(anyString(), anyBoolean(), anyList(), anyList());
  }

  @Test
  void getExpandedSetMembers_cyclicSubsets_terminates() throws Exception {
    // A is a subset of Root and Root is (wrongly) a subset of A.
    when(setRepository.getSubsetEdges(ROOT)).thenReturn(List.of(
      new SetRepository.SubsetEdge(A, "A", ROOT),
      new SetRepository.SubsetEdge(ROOT, "Root", A)));
    when(setRepository.getExpansionFromIris(anyCollection(), eq(false), anyList(), anyList())).thenReturn(Map.of(
      ROOT, setOf(concept("urn:r1", "r1")),
      A, setOf(concept("urn:a1", "a1"))));

    Set<Concept> result = setService.getExpandedSetMembers(ROOT, true, false, true, List.of(), List.of());

    assertEquals(2, result.size());
    assertEquals(Set.of(), containerIris(find(result, "urn:r1")));
    assertEquals(Set.of(A), containerIris(find(result, "urn:a1")));
  }

  @Test
  void getExpandedSetMembers_withoutSubsets_expandsOnlyTheSet() throws Exception {
    when(setRepository.getExpansionFromIri(eq(ROOT), eq(false), anyList(), anyList()))
      .thenReturn(setOf(concept("urn:r1", "r1")));

    Set<Concept> result = setService.getExpandedSetMembers(ROOT, true, false, false, List.of(), List.of());

    assertEquals(1, result.size());
    verify(setRepository, never()).getSubsetEdges(anyString());
  }
}
