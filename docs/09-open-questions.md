# Open Questions

This file tracks decisions that are not settled yet. When a question becomes a decision, move the final reasoning into an ADR.

## Product

- Should HeroLogs keep the name permanently?
- Should the app use an adventure/avatar identity from the first release or keep the MVP visually quieter?
- Should check-ins be part of first launch or introduced after the timeline has value?
- Should weekly recap be in MVP or phase 2?

## Privacy

- Should full local database encryption be required before alpha?
- Should raw location samples ever be persisted?
- Should users be able to export all local data in MVP?
- How should deletion be presented: delete timeline, delete check-ins, delete everything?

## Permissions

- At what moment should background location be requested?
- What functionality should remain when only foreground location is granted?
- Should Health Connect be optional from the first version?
- Should notifications wait until check-ins are implemented?

## Timeline

- What confidence threshold should mark a place as unknown?
- Should place detection be radius-based at first?
- Should the app support manual timeline entry in MVP?
- Should trip correction support only movement type first, or start/end edits too?

## Scoring

- How many days are required before personal baseline scoring starts?
- Should initial scores be hidden until baseline is ready?
- Should dimensions have equal weight at first?
- Should XP reward user corrections?

## Android Implementation

- What minimum SDK should the project target?
- Should the first Android project use package name `com.herologs` or another namespace?
- Should KSP be used from day one for Room/Hilt?
- Should the project include Hilt immediately or start with manual dependency injection?
