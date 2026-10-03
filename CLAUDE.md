# Project 1: Job Posting Extractor & Classifier

## Who I am
- Yash, backend software engineer (Java, Spring Boot, Kafka, GraphQL, PostgreSQL).
- New to GenAI implementation. This project is for LEARNING, then for my portfolio.
- Goal: SWE 1/2 roles at Big Tech (Netflix, Google, Apple, Airbnb, DoorDash).

## How to work with me (important)
- I am here to learn, not to get code written for me. Do NOT generate the whole project at once.
- Before each milestone, explain the GenAI concept in simple language with a real use case.
- Default to guiding questions first. Let me attempt the code, then review it.
- When I write code, critique it: correctness, edge cases, error handling, cleaner alternatives.
- Be direct and honest about weak code. Don't sugarcoat.
- Only write code for me when I explicitly ask, or for boilerplate (build files, config).
- Performance work (caching, rate limiting, latency tuning) is OUT OF SCOPE for now. Flag it as a "later" note, don't implement it.

## What we're building
A Java service that takes a raw job description and returns strict JSON:

```json
{
  "title_level": "SWE II",
  "min_years": 2,
  "sponsorship": "offered | not_offered | not_mentioned",
  "role_type": "backend | fullstack | frontend | mobile | ml | infra | other",
  "tech_stack": ["Java", "Kafka"],
  "match_score": 78,
  "reason": "one or two sentences"
}
```

Company problem this mirrors: turning messy unstructured text (contracts, menus, listings, tickets) into clean structured data.

## Stack
- Java 21, Spring Boot 3, Gradle
- LLM: free, local open models via **Ollama** (v0.14+), using its Anthropic-compatible Messages API at `http://localhost:11434/v1/messages`.
  - Primary model: `qwen2.5:3b` (fits fully in 4 GB VRAM, fast iteration). Comparison model: `qwen2.5:7b`.
  - Hardware: 16 GB RAM, RTX 3050 Laptop (4 GB VRAM).
- Base URL and API key come from env vars (`LLM_BASE_URL`, `LLM_API_KEY`). Never hardcode or commit keys. Local Ollama needs no real key.
- Optional later: run the final eval against Claude Haiku 4.5 (paid, cents) by changing only the base URL, key and model name.
- Start with raw HTTP calls to the Messages API so I learn the request/response shape, then move to Spring AI.

## Milestones
1. **First call.** Call the Messages API with plain Java HTTP client. Understand: model, messages, max_tokens, system prompt, response content blocks, token usage fields.
2. **Prompting.** Extract fields from ONE posting with a system prompt. Experiment with temperature and see what changes.
3. **Structured output.** Force JSON output, map it to a Java record, validate it. Handle malformed JSON (retry once, then fail clearly).
4. **Missing info and hallucination.** Make the model return `not_mentioned` / `null` instead of guessing. Add 2 to 3 few-shot examples.
5. **Test set.** 20 real postings with my hand labels in `testdata/`. Write an evaluator that reports per-field accuracy.
6. **Iterate.** Version prompts (`prompts/v1.txt`, `v2.txt`, ...) and record eval results per version in `RESULTS.md`.
7. **Optional.** Feed live postings from the Greenhouse public API (e.g. board token `stripe`).

## Current status
- [ ] Milestone 1