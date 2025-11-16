# Stage 1 Submission Checklist

## Before Submitting

### GitHub Repository
- [ ] Create private GitHub repository
- [ ] Create `stage1` branch
- [ ] Add lecturer as collaborator
- [ ] Make commits with meaningful messages
- [ ] Push all code to `stage1` branch
- [ ] Optional: Create tag `stage1-submitted`

### README Updates
- [ ] Add both student names
- [ ] Add both student IDs
- [ ] Write Student 1 reflection (100-150 words)
- [ ] Write Student 2 reflection (100-150 words)
- [ ] Fill in contribution matrix with actual commit counts
- [ ] Add Generative AI declaration (if used)
- [ ] Add references (if any)

### Testing
- [ ] Run `mvn test` to ensure all tests pass
- [ ] Run `mvn test jacoco:report` to generate coverage report
- [ ] Verify coverage is ≥35% (check `target/site/jacoco/index.html`)
- [ ] Include coverage report evidence in repo (or note location in README)

### Verification
- [ ] Code compiles: `mvn clean compile`
- [ ] Application runs: `mvn exec:java -Dexec.mainClass="ie.dkit.oop.LeaderboardApp"`
- [ ] All 10 rows load correctly
- [ ] Sorting works (by score and by accuracy)
- [ ] Safe removal demonstration works

### Files to Include
- [x] All source code files
- [x] All test files
- [x] `data/sample_10.csv` (10 rows)
- [x] `pom.xml`
- [x] `README.md` (complete)
- [x] `.gitignore`

### Moodle Submission
- [ ] Submit GitHub repository URL on Moodle
- [ ] Ensure URL points to `stage1` branch
- [ ] Verify lecturer can access the repository

## Quick Test Commands

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Generate coverage
mvn test jacoco:report

# Run application
mvn exec:java -Dexec.mainClass="ie.dkit.oop.LeaderboardApp"
```

Good luck with your submission! 🎯

