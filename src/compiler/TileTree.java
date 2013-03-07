package compiler;


import ir.cfg.BasicBlock;


public class TileTree {
    public Tile rootTile;
    
    public TileTree() {
        rootTile = new Tile(null, 0);
    }

    public Tile smallestTileOf(BasicBlock block){
        //TODO follow block up stack to smallest tile.
        return rootTile.smallestTileOf(block);
    }



}
//Tiles are visited in a bottom up fashion and 
// a local interference graph is created and 
// colored (using pseudo registers) for each tile.