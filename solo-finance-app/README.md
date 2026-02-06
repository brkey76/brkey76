# Solo Finance Leveling (APK Android)

Aplicativo Android em Kotlin + Jetpack Compose inspirado no conceito de progressão de **Solo Leveling**, focado em organização financeira.

## Funcionalidades iniciais

- Cadastro de:
  - Salário
  - Renda extra
  - Gasto planejado
  - Gasto não planejado
  - Investimento
- Cálculo de saldo atual.
- Sistema de pontos por ação financeira.
- Sistema de nível para manter o usuário motivado com progresso.
- Histórico dos lançamentos.

## Regra de pontuação

- Salário: +10
- Renda extra: +15
- Gasto planejado: +5
- Gasto não planejado: -10
- Investimento: +20

## Como gerar o APK

1. Instale Android Studio (Hedgehog+).
2. Abra a pasta `solo-finance-app`.
3. Aguarde sync do Gradle.
4. Execute:

```bash
./gradlew assembleDebug
```

APK gerado em:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Próximos passos recomendados

- Persistência local (Room).
- Metas mensais e streak diário.
- Missões semanais e conquistas.
- Backup em nuvem com Firebase.
