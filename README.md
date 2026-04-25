# CobblemonIVGuarantee

> Garante que Pokémon lendários, míticos, Ultra Beasts, Paradox e fósseis spawnem
> com pelo menos **3 IVs perfeitos (31)** ao aparecerem no mundo.

---

## Como funciona

O mod usa um **Mixin** em `MobEntity.initialize()` — método do Minecraft chamado
**apenas no spawn**, nunca ao carregar entidades salvas. Isso garante que:

- Pokémon já capturados **não são alterados**.
- Apenas o spawn natural (e via comando/spawner) é interceptado.
- A compatibilidade com **Cobblemon Spawn Legendaries** é automática: qualquer
  Pokémon cujo JSON de espécie contenha um dos labels abaixo receberá o bônus.

### Labels detectados

| Label no JSON de espécie | Categoria      |
|--------------------------|----------------|
| `legendary`              | Lendário       |
| `mythical`               | Mítico         |
| `ultra_beast` / `ultrabeast` | Ultra Beast |
| `paradox`                | Paradox        |
| `fossil`                 | Fóssil         |

---

## Pré-requisitos

| Ferramenta | Versão recomendada |
|------------|--------------------|
| JDK        | 21                 |
| Gradle     | 8.8 (via wrapper)  |
| Git        | qualquer           |

---

## Configuração inicial (uma única vez)

### 1. Instalar o Gradle Wrapper

O repositório **não inclui** o arquivo binário `gradle-wrapper.jar` (é um binário).
Execute o comando abaixo na pasta raiz do projeto para gerá-lo:

```bash
gradle wrapper --gradle-version 8.8
```

> Se não tiver o Gradle instalado globalmente, baixe em https://gradle.org/install/

### 2. Verificar a versão do Cobblemon

Abra `gradle.properties` e confirme que `cobblemon_version` corresponde à versão
que você tem instalada. Consulte as versões disponíveis em:

    https://maven.cobblemon.dev/releases/com/cobblemon/fabric/

---

## Compilação

```bash
# Na pasta raiz do projeto:
./gradlew build          # Linux / macOS
gradlew.bat build        # Windows
```

O `.jar` compilado ficará em:

    build/libs/cobblemonivguarantee-1.0.0.jar

---

## Instalação

1. Copie o `.jar` para a pasta `/mods` do seu servidor ou cliente Fabric.
2. Certifique-se de que as seguintes dependências também estão em `/mods`:
   - `fabric-api-*.jar`
   - `cobblemon-fabric-*.jar`
   - `cobblemon-spawn-legendaries-*.jar` *(se usar)*
3. Inicie o servidor normalmente.

No console você verá:

    [CobblemonIVGuarantee] Mod carregado! Pokémon lendários...

---

## Verificando em jogo

Capture um lendário e use o comando abaixo para conferir os IVs:

```
/pokemoninfo   (ou o comando equivalente do Cobblemon)
```

Ou instale o mod **Cobblemon EV/IV Display** para visualizar os valores diretamente.

---

## Personalização

### Mudar o número mínimo de IVs perfeitos

No arquivo `MobEntityInitializeMixin.java`, altere a constante:

```java
private static final int MIN_PERFECT_IVS = 3; // ← mude aqui (ex: 5 para 5 IVs)
```

### Adicionar/remover categorias

Na mesma classe, edite o `Set`:

```java
private static final Set<String> SPECIAL_LABELS = Set.of(
    "legendary",
    "mythical",
    "ultra_beast",
    "ultrabeast",
    "paradox",
    "fossil"
    // "pseudo_legendary",  ← exemplo de categoria customizada
);
```

Os labels devem corresponder exatamente aos valores no JSON de espécie do Cobblemon
(em minúsculo).

---

## Estrutura do projeto

```
CobblemonIVGuarantee/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradle/wrapper/
│   └── gradle-wrapper.properties
└── src/main/
    ├── java/com/cobblemonivguarantee/
    │   ├── CobblemonIVGuaranteeMod.java      ← entrypoint
    │   └── mixin/
    │       └── MobEntityInitializeMixin.java  ← lógica de IVs
    └── resources/
        ├── fabric.mod.json
        └── cobblemonivguarantee.mixins.json
```

---

## Licença

MIT — livre para usar, modificar e distribuir.
