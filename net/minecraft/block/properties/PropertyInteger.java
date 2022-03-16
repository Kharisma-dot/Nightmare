package net.minecraft.block.properties;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;

public class PropertyInteger extends PropertyHelper<Integer>
{
    private final ImmutableSet<Integer> allowedValues;

    protected PropertyInteger(String name, int min, int max)
    {
        super(name, Integer.class);

        if (min < 0)
            throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
        else if (max <= min)
            throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
        else
        {
            Set<Integer> set = Sets.<Integer>newHashSet();

            for (int i = min; i <= max; ++i)
                set.add(i);

            this.allowedValues = ImmutableSet.copyOf(set);
        }
    }

    public Collection<Integer> getAllowedValues()
    {
        return this.allowedValues;
    }

    public boolean equals(Object object)
    {
        if (this == object)
            return true;
        else if (object != null && this.getClass() == object.getClass())
        {
            if (!super.equals(object))
                return false;
            else
            {
                PropertyInteger propertyinteger = (PropertyInteger)object;
                return this.allowedValues.equals(propertyinteger.allowedValues);
            }
        }
        else
            return false;
    }

    public int hashCode()
    {
        int i = super.hashCode();
        i = 31 * i + this.allowedValues.hashCode();
        return i;
    }

    public static PropertyInteger create(String name, int min, int max)
    {
        return new PropertyInteger(name, min, max);
    }

    /**
     * Get the name for the given value.
     */
    public String getName(Integer value)
    {
        return value.toString();
    }
}
