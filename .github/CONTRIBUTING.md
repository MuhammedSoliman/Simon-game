# Contributing to Simon Game 🎮

Thanks for your interest in contributing! Here's how to get started.

## Getting Started

1. **Fork** the repository
2. **Clone** your fork: `git clone https://github.com/YOUR_USERNAME/simon-game-android.git`
3. Create a new branch: `git checkout -b feature/your-feature-name`
4. Make your changes, then push and open a **Pull Request**

## Code Style

- Follow standard **Java naming conventions** (camelCase methods, PascalCase classes)
- Keep methods short and focused (single responsibility)
- Add comments for non-obvious logic
- Ensure no compiler warnings before submitting

## What to Work On

Check the [issues tab](../../issues) for open bugs and feature requests. Good first issues are labeled `good first issue`.

Ideas from the README roadmap are especially welcome:
- Difficulty levels (speed scaling)
- Cloud leaderboard (Firebase)
- Haptic feedback
- Accessibility / TalkBack support

## Commit Message Format

Use conventional commits:

```
feat: add difficulty levels
fix: correct sequence timing after round 5
docs: update README with new screenshots
style: remove trailing whitespace
```

## Pull Request Guidelines

- Target the `main` branch
- Include a short description of the change and why it's needed
- Attach a screenshot or screen recording if changing UI
- Make sure `./gradlew lint` passes before submitting

## Reporting Bugs

Open an issue with:
- Device model & Android version
- Steps to reproduce
- Expected vs actual behavior
- Screenshot if applicable

---

Thank you for helping make Simon Game better! 🕹️
