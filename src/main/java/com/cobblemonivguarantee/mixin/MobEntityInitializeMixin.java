package com.cobblemonivguarantee.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.cobblemonivguarantee.CobblemonIVGuaranteeMod;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Handles naturally spawned Pokemon (wild spawns, /summon, spawners).
 * Fossil Pokemon revived via the Fossil Machine are handled separately via
 * CobblemonEvents.FOSSIL_REVIVED in CobblemonIVGuaranteeMod.onInitialize().
 */
@Mixin(MobEntity.class)
public abstract class MobEntityInitializeMixin {

    @Inject(
        method = "initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;",
        at = @At("RETURN")
    )
    private void cobblemonivguarantee$onInitialize(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            EntityData entityData,
            CallbackInfoReturnable<EntityData> cir) {

        if (!((Object) this instanceof PokemonEntity pokemonEntity)) return;

        Pokemon pokemon = pokemonEntity.getPokemon();
        if (pokemon == null) return;

        Species species = pokemon.getSpecies();
        if (species == null) return;

        if (!CobblemonIVGuaranteeMod.isSpecialPokemon(species)) return;

        CobblemonIVGuaranteeMod.guaranteeMinPerfectIVs(pokemon.getIvs(), CobblemonIVGuaranteeMod.MIN_PERFECT_IVS);

        CobblemonIVGuaranteeMod.LOGGER.info(
            "[CobblemonIVGuarantee] {} spawnou com IVs garantidos.",
            species.getName()
        );
    }
}
