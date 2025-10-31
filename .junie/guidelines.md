# Working with QOF→IMQ Transformation Tasks

## Quick Start

This document provides concise instructions for working with the task list defined in `docs/tasks.md`.

---

## Understanding the Structure

### Task Organization

Tasks are organized into **8 phases** following a logical development progression:

1. **Phase 1: Foundation & Infrastructure** - Establish core architecture and utilities
2. **Phase 2: Core Transformation Engine** - Implement main transformation logic
3. **Phase 3: Engine Integration & Orchestration** - Assemble components into a unified system
4. **Phase 4: Output and Serialization** - Handle output format and validation
5. **Phase 5: Batch Processing and Scalability** - Enable bulk transformations
6. **Phase 6: Tooling and Integration** - Provide operational interfaces
7. **Phase 7: Documentation and Testing** - Ensure quality and maintainability
8. **Phase 8: Deployment and Operations** - Prepare for production

### Task Numbering Scheme

- **Phase Number** (1-8)
- **Section Number** within phase (1-6)
- **Individual Task** (incrementing within section)

Example: `1.2.5` means Phase 1, Section 2 (QOF Model Extension), Task 5

### Linking Information

Each major section includes:
- **Plan Link**: Reference to implementation plan (e.g., `Plan 1.1`)
- **Requirement Links**: Reference to requirements (e.g., `REQ-7`)

---

## Marking Task Completion

### Checkbox Format

Tasks use standard Markdown checkbox format:

```markdown
- [ ] Task to do (incomplete)
- [x] Task completed (complete)
```

### Completing a Task

1. **Edit `docs/tasks.md`** and change the checkbox from `[ ]` to `[x]`
2. **Verify** the task is actually complete (code written, reviewed, tested)
3. **Commit** your changes with a message like:
   ```
   Complete task 2.3.1: Create SelectionTransformer component
   ```

### Tracking Progress

To see overall progress:
- Count total tasks: Look for all `- [ ]` and `- [x]` entries
- Count completed: Look for `- [x]` entries only
- Calculate percentage: `(completed / total) * 100`

---

## Adding New Tasks

When new requirements emerge or scope changes:

1. **Identify the appropriate phase and section** where the new task belongs
2. **Add the task** in the correct location with checkbox format:
   ```markdown
   - [ ] New task description
   ```
3. **Maintain proper linking** to plan and requirements:
   - Add reference to relevant plan item (if existing)
   - Add reference to requirement(s) if new
4. **Keep formatting consistent** with existing tasks
5. **Update the task count** if tracking totals

### Example: Adding a Task to Phase 2.3

```markdown
### 2.3 Selection Criteria to Where Clause Transformation (Plan 2.3 → REQ-3, REQ-7)

- [ ] Create `SelectionTransformer` component
- [ ] Implement Selection → Match conversion
- [ ] NEW: Create custom exception for invalid selection criteria  ← NEW TASK
- [ ] Create SelectionRule to Match conversion
```

---

## Modifying Existing Tasks

If a task needs adjustment:

1. **Update the task description** to reflect the new understanding
2. **Adjust the checkbox** if completion status changed
3. **Update links** if it now relates to different requirements
4. **Add a note** in git commit explaining the change

Example commit message:
```
Update task 3.1.2: Add context injection mechanism requirement
```

---

## Phase Workflow Recommendations

### Before Starting Phase 2
- [ ] All Phase 1 tasks completed and code reviewed
- [ ] Dependencies in place (utilities, builders, error handling)

### Before Starting Phase 3
- [ ] All Phase 2 tasks completed and tested
- [ ] Component transformers working independently
- [ ] Core transformation logic validated

### Before Starting Phase 4
- [ ] Phase 3 orchestration tested end-to-end
- [ ] All component transformations verified

### Before Starting Phase 5
- [ ] Phase 4 serialization and validation working
- [ ] Output format validated

### Before Starting Phase 6
- [ ] Phase 5 batch processing complete
- [ ] Performance targets met

### Before Starting Phase 7
- [ ] Phase 6 tooling and integration complete
- [ ] Code is feature-complete

### Before Starting Phase 8
- [ ] Phase 7 testing achieves 80%+ coverage
- [ ] All documentation complete
- [ ] Code reviewed and approved

---

## Testing Integration

For each task involving code implementation:

1. **Write tests** as part of completing the task
2. **Mark the task complete** only when tests pass
3. **Verify coverage** targets are met for that component

Related test tasks:
- **Unit Testing** (Phase 7.2): Tests for individual components
- **Integration Testing** (Phase 7.3): End-to-end tests
- **Performance Testing** (Phase 7.4): Performance validation

---

## Documentation Synchronization

When completing tasks:

1. **Update code comments** explaining the implementation
2. **Update mapping documentation** (Phase 7.1) if transformation rules changed
3. **Add examples** to documentation for complex logic
4. **Update README** or user guides if adding new features

---

## Dependency Management

Phases should be completed in sequence, but tasks within a phase can be reordered:

**Hard Dependencies** (must complete in order):
```
Phase 1 → Phase 2 → Phase 3 → Phase 4 → Phase 5
                     ↓
                  Phase 6 ← Phase 7 ← Phase 8
```

**Flexible within Phases**: Tasks in the same phase can often be parallelized.

---

## Common Task Patterns

### For Component Creation Tasks

1. Create the component class
2. Add basic structure and dependencies
3. Implement core logic
4. Write unit tests
5. Write integration tests
6. Add documentation
7. Complete the task checkbox

### For Testing Tasks

1. Create test class with appropriate naming (`ComponentTest` convention)
2. Add test method for normal case
3. Add test methods for edge cases
4. Add test methods for error scenarios
5. Run full test suite to ensure no regressions
6. Complete the task checkbox

### For Documentation Tasks

1. Create or update document
2. Add content with clear structure
3. Include examples where appropriate
4. Review for completeness
5. Link to related documentation
6. Complete the task checkbox

---

## Issue Tracking

Link task completion to issue tracking:

- **GitHub Issues**: Reference issue numbers in commit messages
  ```
  Complete task 2.3.1 - Fixes #123
  ```

- **Jira**: Link task to story/epic
  ```
  Complete task 2.3.1 [IMAPI-456]
  ```

---

## Progress Reporting

### Weekly Status Format

Include task counts in status updates:

```markdown
## Phase 1: Foundation (15 tasks)
- Completed: 12
- In Progress: 2
- Not Started: 1
- Completion: 80%

## Phase 2: Core Engine (26 tasks)
- Completed: 0
- In Progress: 0
- Not Started: 26
- Completion: 0%

Overall Project: 35% complete (12/68 tasks)
```

### Milestone Completion

When all tasks in a phase complete:
1. Create a release tag
2. Generate release notes highlighting phase completion
3. Update project README with new capabilities

---

## Troubleshooting

### Task Unclear?
- Review the linked requirement (REQ-X)
- Review the linked plan section (Plan X.Y)
- Check mapping documentation in Phase 7.1
- Ask on project communication channel

### Dependencies Not Ready?
- Check if prerequisites from earlier phase are complete
- Review dependency diagram in `docs/plan.md`
- Consider marking task as "blocked" with reason

### Can't Meet Performance Targets?
- Document findings in Phase 5.3 performance optimization
- Add optimization recommendations
- Consider architectural changes if needed

---

## Best Practices

✅ **DO:**
- Keep tasks atomic and completable in 1-2 days
- Link every task to at least one requirement
- Update task list when scope changes
- Review tasks before and after completing
- Use consistent formatting

❌ **DON'T:**
- Leave tasks marked incomplete when they're actually done
- Add vague tasks without clear acceptance criteria
- Change phase structure without planning update
- Mark task complete without proper testing/review
- Forget to update related documentation

---

## Quick References

### View Current Task Status
```bash
grep -c "^- \[x\]" docs/tasks.md   # Completed tasks
grep -c "^- \[ \]" docs/tasks.md   # Remaining tasks
```

### Search for Tasks in Phase X
```bash
grep "^### X\." docs/tasks.md      # Find section headers
```

### Find Tasks for Requirement Y
```bash
grep "REQ-Y" docs/tasks.md         # Find all REQ-Y tasks
```

---

## Contact & Questions

For questions about:
- **Task content**: Check the linked plan and requirements
- **Phase progression**: Review the dependency diagram
- **Implementation approach**: Check the mapping documentation
- **General questions**: Consult the project team

---

**Last Updated**: 2025  
**Version**: 1.0