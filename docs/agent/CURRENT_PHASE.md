# P4C: Local Data Controls

Status: `IN_PROGRESS`

Branch: `feature/p4c-preferences-cleaner`

## Objective

Before collecting sensitive signals, add a Privacy/Data screen that accurately
lists stored categories and provides a confirmed **delete all local HeroLogs
data** flow.

## Scope

- List the categories currently stored by Room and DataStore.
- Add a confirmed delete-all flow that clears Room data and relevant DataStore
  preferences without relying on uninstall.
- Add repository/use-case tests for deletion behavior.
- Update privacy documentation to match the implemented controls.

## Out Of Scope

- Export.
- Encryption.
- Cloud backup.
- Selective deletion.
