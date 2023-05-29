
How to define a new machine:
- definition of the machine in `data/<your_namespace>/techarium/machine/<machine_id>.json`
- machine deploy animation in `assets/<your_namespace>/animations/<machine_id>.animation.json`
- machine geo animation in `assets/<your_namespace>/geo/<machine_id>.geo.json`
- machine texture animation in `assets/<your_namespace>/textures/block/animated/<machine_id>.png`
- blockstate, model and textures of the block as usual
- textures of the gui as you defined in the machine definition

Gravy's explanation on how to implement the machines registries

https://discord.com/channels/880995984426020885/980249463652225075/1008490390225174638

Definition of the machine:
```json5
{
	// array of inventory modules for this machine
	"modules": [
		{
			"type": "energy", // ["energy", "item", "fluid", "gas"]
			// units : energy = rf, item = slot, fluid and gas = bucket
			"capacity": 100, 
            "input-rate": 10, // we're adding io rates latter lol
            "output-rate": 8,
		}
	],
	"size": [ 1, 2, 1 ]  // size of the machine when it is deployed. order is [ x, y, z ]
}
```

Machine gui definition

```json5
{
	"inventory-x": 200,  // x position of the player inventory
	"inventory-y": 200,  // y position of the player inventory
	"width": 200,  // width of the texture
	"height": 256,  // height of the texture
	"texture": "techarium:gui/botarium/botarium.png",  // texture to use
	"modules": [
		// array of modules to display, they use the same order defined in the machine json
		// (meaning the first module defined in the machine json will use the first element in this array to be displayed)
		{
			"x": 10,  // x position in the inventory
			"y": 10,  // y position in the inventory
			"height": 20,  // height of the module
			"width": 20,  // width of the module
			"x-offset": 5,  // (optional, default=0) x offset of the texture in the texture file
			"y-offset": 100,  // (optional, default=0) y offset of the texture in the texture file
			"texture-override": "techarium:gui/botarium/botarium_components.png"  // (optional) use this texture instead of the main texture
		}
	]
}
```
