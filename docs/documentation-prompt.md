# Documentation Generation Task

Your objective is to document this entire repository.

You have access to:

- the complete source code
- every folder
- every configuration file
- every commit in Git history
- every branch merged into main
- package.json
- Astro configuration
- Tailwind configuration
- assets
- Markdown content
- components
- layouts

Your job is to reverse engineer the project and produce professional documentation.

## Analyze

Analyze every part of the repository including:

- Git history
- Commit messages
- Folder structure
- Architecture
- Components
- Styling
- Images
- Icons
- Content Collections
- Markdown content
- Astro pages
- Public assets
- Configuration files

---

## Produce

Generate the following markdown documents.

### PROJECT_DOCUMENTATION.md

Include:

- Executive Summary
- Purpose of the project
- Technologies used
- Project goals
- Website overview
- Major features
- Current status

---

### DEVELOPMENT_HISTORY.md

Read every Git commit.

Produce a chronological timeline explaining:

- major milestones
- feature additions
- refactors
- bug fixes
- design changes
- deployment improvements

If information is missing, infer the most likely development sequence based on the repository.

Clearly label inferred information.

---

### ARCHITECTURE.md

Describe:

- folder structure
- routing
- Astro architecture
- layouts
- components
- content collections
- assets
- public folder
- scripts
- utilities

Explain why each folder exists.

---

### DESIGN_SYSTEM.md

Document:

- color palette
- typography
- spacing
- gradients
- icons
- buttons
- cards
- animations
- responsive breakpoints

If colors are not explicitly documented, infer them from the codebase.

---

### CONTENT_STRUCTURE.md

Explain:

- Markdown collections
- frontmatter
- schema
- plant content
- images
- taxonomies
- relationships

---

### DEPLOYMENT.md

Explain:

- build process
- npm scripts
- hosting
- CI/CD (if any)
- optimization

---

### CHANGELOG.md

Generate a human-readable changelog from Git history.

---

## Documentation style

Use professional technical writing.

Explain decisions.

Include Mermaid diagrams where appropriate.

Generate tables whenever useful.

Use Markdown only.

If information cannot be found:

- infer it from the repository
- mark it as inferred
- never invent impossible facts

The final documentation should be detailed enough for a new developer to fully understand the project without speaking to its original authors.