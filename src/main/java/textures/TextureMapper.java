package textures;

import java.util.*;

public class TextureMapper
{
    public class MappedTexture
    {
        public int baseID;
        public OPTexture texture;
        public int mappedID;

        public MappedTexture(int baseID, int mappedID, OPTexture texture)
        {
            this.baseID = baseID;
            this.mappedID = mappedID;
            this.texture = texture;
        }
    }

    private final Map<OPTexture, MappedTexture> textures;
    private final Map<Integer, Set<Integer>> remappedTexturesIDs;

    public TextureMapper()
    {
        this.textures = new HashMap<>();
        this.remappedTexturesIDs = new HashMap<>();

        for (MappedTexture tx : buildRemappings())
        {
            this.textures.put(tx.texture, tx);

            if (!this.remappedTexturesIDs.containsKey(tx.baseID))
                this.remappedTexturesIDs.put(tx.baseID, new HashSet<>());

            this.remappedTexturesIDs.get(tx.baseID).add(tx.mappedID);
        }
    }

    public Collection<Integer> getRemappedIDs(int baseID)
    {
        return this.remappedTexturesIDs.getOrDefault(baseID, Collections.emptySet());
    }
    public MappedTexture getMappedTexture(OPTexture texture)
    {
        return this.textures.get(texture);
    }

    // En este método se añaden TODOS LOS REMAPEOS A LA LISTA
    private MappedTexture[] buildRemappings()
    {

        return new MappedTexture[]{
            new MappedTexture(1003,2000, OPTexture.OPE_OPE_STOLEN_HEART)
        };
    }
}
