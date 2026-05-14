---
name: course-index-builder
description: Build structured Markdown course knowledge indexes from lecture files or notes. Use when Codex needs to synthesize a course into thematic knowledge blocks instead of lecture-by-lecture notes, with lecture source tags, prerequisite foundations, and beyond-lecture extensions.
---

# Course Index Builder

## Goal

Build a single Markdown knowledge index for a course. Treat it as a structured knowledge map, not a set of lecture summaries.

Prefer thematic organization over chronological lecture order. Merge related lecture concepts into larger blocks, then arrange those blocks from foundations to applications.

## Default Output

Create or update one Markdown file unless the user asks otherwise. Use a filename such as `index.md`, `course-index.md`, or the user's requested name.

Use the user's preferred language. If unspecified, use Chinese for explanations and keep important technical terms in English.

## Tagging Rules

Use source tags consistently:

- `[lecNN]`: Concept is covered by lecture NN.
- `[lecNN, lecMM]`: Concept connects multiple lectures.
- `[Prereq]`: Foundation knowledge needed to understand the block, but not clearly covered by the listed lectures.
- `[Beyond Lec]`: Advanced, newer, or out-of-scope concept that naturally follows from the block.

Every in-course concept must include at least one lecture tag. Prerequisites and beyond-lecture extensions should be clearly separated from in-course concepts.

## Workflow

1. Confirm the intended scope, output path, and permission to read source files.
2. Inspect filenames first to infer the course arc.
3. When permitted, read lecture materials only for extraction and synthesis.
4. Draft a thematic architecture before writing the index.
5. Merge similar ideas into knowledge blocks, such as algorithms, analysis, recursion, graphs, trees, sorting, heaps, and search structures.
6. For each block, list core concepts in a hierarchy from broad ideas to specific tools.
7. Add prerequisite foundations and beyond-lecture extensions at the end of each block.
8. Add cross-links between blocks where one idea depends on or motivates another.
9. Validate that the final index is not a lecture-by-lecture summary.

## Block Structure

Use this structure for each major knowledge block when appropriate:

```md
## Knowledge Block Name

Covered lectures: [lecNN, lecMM]

### Core Structure
- Broad concept
  - Specific concept: short purpose or effect. [lecNN]
  - Related method: short purpose or effect. [lecMM]

### Prerequisite Foundations
- Foundation name: why it matters here. [Prereq]

### Beyond-Lecture Extensions
- Advanced method name: short effect or why it matters. [Beyond Lec]

### Connections
- Link to another block or concept, with lecture tags when it is course-covered.
```

Keep each bullet concise. Do not write full tutorials unless the user asks for expansion.

## Thematic Synthesis Guidelines

Do not structure the index as:

```md
## Lec01
## Lec02
## Lec03
```

Instead, synthesize into layered blocks. For example:

```md
## Graphs and Traversal

Covered lectures: [lec07, lec08]

### Core Structure
- Graph model: vertices, edges, paths, cycles, connectedness. [lec07]
- Representation: adjacency matrix and adjacency list. [lec07]
- BFS: level-order exploration, useful for reachability and unweighted shortest paths. [lec08]
- DFS: depth-first exploration, useful for reachability, cycle reasoning, and recursive graph structure. [lec08]

### Prerequisite Foundations
- Sets and relations: graph edges can be understood as relationships over vertices. [Prereq]
- Queue and stack behavior: BFS depends on queues; DFS depends on stacks or recursion. [Prereq]

### Beyond-Lecture Extensions
- Dijkstra's algorithm: shortest paths with non-negative edge weights. [Beyond Lec]
- Topological sort: dependency ordering in directed acyclic graphs. [Beyond Lec]
```

## Prerequisite Extensions

Use prerequisite notes to expose foundations the course may rely on implicitly, especially:

- Discrete mathematics: sets, relations, functions, proofs, induction, graphs, trees.
- Algebra and arithmetic: logarithms, exponentials, summations, inequalities.
- Proof methods: contradiction, induction, loop invariants, correctness arguments.
- Programming foundations: arrays, linked structures, stacks, queues, recursion, memory model basics.

Mention only prerequisites that directly support the current block.

## Beyond-Lecture Extensions

Use beyond-lecture notes as signposts, not explanations. Each item should be a name plus a short effect, such as:

- `Dijkstra's algorithm`: shortest paths for non-negative weighted graphs. [Beyond Lec]
- `AVL tree`: self-balancing BST with logarithmic search, insert, and delete. [Beyond Lec]
- `Merge sort`: divide-and-conquer sorting with stable `O(n log n)` behavior. [Beyond Lec]

Do not let advanced material dominate the course index.

## Quality Checklist

Before finishing, verify:

- The output is one coherent Markdown index.
- The structure is thematic, not lecture-by-lecture.
- Every course concept has lecture source tags.
- Prerequisites are marked `[Prereq]`.
- Advanced extensions are marked `[Beyond Lec]`.
- Explanations are concise and useful, not full lecture notes.
- The index shows relationships between concepts, not just a flat list.
