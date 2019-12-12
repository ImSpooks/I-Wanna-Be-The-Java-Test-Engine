var Types = ["BLOCKS", "ITEMS", "KILLERS", "MISC", "PLATFORMS", "SAVES", "SLOPES", "TRIGGERS", "WARPS"];
var Resources = {
   sprBlock: {
       path: "sprites/blocks/default/sprBlock.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock2: {
       path: "sprites/blocks/default/sprBlock2.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock3: {
       path: "sprites/blocks/default/sprBlock3.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock4: {
       path: "sprites/blocks/default/sprBlock4.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock5: {
       path: "sprites/blocks/default/sprBlock5.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock6: {
       path: "sprites/blocks/default/sprBlock6.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock7: {
       path: "sprites/blocks/default/sprBlock7.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock8: {
       path: "sprites/blocks/default/sprBlock8.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock9: {
       path: "sprites/blocks/default/sprBlock9.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock10: {
       path: "sprites/blocks/default/sprBlock10.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock11: {
       path: "sprites/blocks/default/sprBlock11.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock12: {
       path: "sprites/blocks/default/sprBlock12.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock13: {
       path: "sprites/blocks/default/sprBlock13.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock14: {
       path: "sprites/blocks/default/sprBlock14.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBlock15: {
       path: "sprites/blocks/default/sprBlock15.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprMiniblock: {
       path: "sprites/blocks/default/sprMiniblock.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprSaveBlocker: {
       path: "sprites/blocks/default/sprSaveBlocker.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprSlideBlock: {
       path: "sprites/blocks/default/sprSlideBlock.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprSlideBlockL: {
       path: "sprites/blocks/default/sprSlideBlockL.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprSlideBlockR: {
       path: "sprites/blocks/default/sprSlideBlockR.png",
       parent: "SPRITES",
       type: "BLOCKS",
       subtype: "default"
   },
   sprBossItem: {
       path: "sprites/items/default/sprBossItem.png",
       parent: "SPRITES",
       type: "ITEMS",
       subtype: "default"
   },
   sprSecretItem: {
       path: "sprites/items/default/sprSecretItem.png",
       parent: "SPRITES",
       type: "ITEMS",
       subtype: "default"
   },
   sprCherry: {
       path: "sprites/killers/default/sprCherry.gif",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprCherryWhite: {
       path: "sprites/killers/default/sprCherryWhite.gif",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprKillerBlock: {
       path: "sprites/killers/default/sprKillerBlock.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprMiniDown: {
       path: "sprites/killers/default/sprMiniDown.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprMiniLeft: {
       path: "sprites/killers/default/sprMiniLeft.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprMiniRight: {
       path: "sprites/killers/default/sprMiniRight.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprMiniUp: {
       path: "sprites/killers/default/sprMiniUp.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprSpikeDown: {
       path: "sprites/killers/default/sprSpikeDown.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprSpikeLeft: {
       path: "sprites/killers/default/sprSpikeLeft.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprSpikeRight: {
       path: "sprites/killers/default/sprSpikeRight.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   sprSpikeUp: {
       path: "sprites/killers/default/sprSpikeUp.png",
       parent: "SPRITES",
       type: "KILLERS",
       subtype: "default"
   },
   playerstart: {
       path: "sprites/misc/default/playerstart.png",
       parent: "SPRITES",
       type: "MISC",
       subtype: "default"
   },
   sprGravityDown: {
       path: "sprites/misc/default/sprGravityDown.png",
       parent: "SPRITES",
       type: "MISC",
       subtype: "default"
   },
   sprGravityUp: {
       path: "sprites/misc/default/sprGravityUp.png",
       parent: "SPRITES",
       type: "MISC",
       subtype: "default"
   },
   sprJumpRefresher: {
       path: "sprites/misc/default/sprJumpRefresher.png",
       parent: "SPRITES",
       type: "MISC",
       subtype: "default"
   },
   sprSign: {
       path: "sprites/misc/default/sprSign.png",
       parent: "SPRITES",
       type: "MISC",
       subtype: "default"
   },
   sprWalljumpL: {
       path: "sprites/misc/default/sprWalljumpL.png",
       parent: "SPRITES",
       type: "MISC",
       subtype: "default"
   },
   sprWalljumpR: {
       path: "sprites/misc/default/sprWalljumpR.png",
       parent: "SPRITES",
       type: "MISC",
       subtype: "default"
   },
   sprWater: {
       path: "sprites/misc/default/sprWater.png",
       parent: "SPRITES",
       type: "MISC",
       subtype: "default"
   },
   sprMovingPlatform: {
       path: "sprites/platforms/default/sprMovingPlatform.png",
       parent: "SPRITES",
       type: "PLATFORMS",
       subtype: "default"
   },
   sprPlatform: {
       path: "sprites/platforms/default/sprPlatform.png",
       parent: "SPRITES",
       type: "PLATFORMS",
       subtype: "default"
   },
   sprSave: {
       path: "sprites/saves/default/sprSave.png",
       parent: "SPRITES",
       type: "SAVES",
       subtype: "default"
   },
   sprSaveFlip: {
       path: "sprites/saves/default/sprSaveFlip.png",
       parent: "SPRITES",
       type: "SAVES",
       subtype: "default"
   },
   sprSaveMedium: {
       path: "sprites/saves/default/sprSaveMedium.png",
       parent: "SPRITES",
       type: "SAVES",
       subtype: "default"
   },
   sprSaveMediumFlip: {
       path: "sprites/saves/default/sprSaveMediumFlip.png",
       parent: "SPRITES",
       type: "SAVES",
       subtype: "default"
   },
   sprSaveVHard: {
       path: "sprites/saves/default/sprSaveVHard.png",
       parent: "SPRITES",
       type: "SAVES",
       subtype: "default"
   },
   sprSaveVHardFlip: {
       path: "sprites/saves/default/sprSaveVHardFlip.png",
       parent: "SPRITES",
       type: "SAVES",
       subtype: "default"
   },
   sprSlopeBlock: {
       path: "sprites/slopes/default/sprSlopeBlock.png",
       parent: "SPRITES",
       type: "SLOPES",
       subtype: "default"
   },
   sprSlopeDownLeft: {
       path: "sprites/slopes/default/sprSlopeDownLeft.png",
       parent: "SPRITES",
       type: "SLOPES",
       subtype: "default"
   },
   sprSlopeDownRight: {
       path: "sprites/slopes/default/sprSlopeDownRight.png",
       parent: "SPRITES",
       type: "SLOPES",
       subtype: "default"
   },
   sprSlopeUpLeft: {
       path: "sprites/slopes/default/sprSlopeUpLeft.png",
       parent: "SPRITES",
       type: "SLOPES",
       subtype: "default"
   },
   sprSlopeUpRight: {
       path: "sprites/slopes/default/sprSlopeUpRight.png",
       parent: "SPRITES",
       type: "SLOPES",
       subtype: "default"
   },
   sprButton_strip2: {
       path: "sprites/triggers/default/sprButton_strip2.png",
       parent: "SPRITES",
       type: "TRIGGERS",
       subtype: "default"
   },
   sprSpikeTriggerDown: {
       path: "sprites/triggers/default/sprSpikeTriggerDown.png",
       parent: "SPRITES",
       type: "TRIGGERS",
       subtype: "default"
   },
   sprSpikeTriggerLeft: {
       path: "sprites/triggers/default/sprSpikeTriggerLeft.png",
       parent: "SPRITES",
       type: "TRIGGERS",
       subtype: "default"
   },
   sprSpikeTriggerRight: {
       path: "sprites/triggers/default/sprSpikeTriggerRight.png",
       parent: "SPRITES",
       type: "TRIGGERS",
       subtype: "default"
   },
   sprSpikeTriggerUp: {
       path: "sprites/triggers/default/sprSpikeTriggerUp.png",
       parent: "SPRITES",
       type: "TRIGGERS",
       subtype: "default"
   },
   sprTriggerMask: {
       path: "sprites/triggers/default/sprTriggerMask.png",
       parent: "SPRITES",
       type: "TRIGGERS",
       subtype: "default"
   },
   sprWarp: {
       path: "sprites/warps/default/sprWarp.png",
       parent: "SPRITES",
       type: "WARPS",
       subtype: "default"
   }
};
