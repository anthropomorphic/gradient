package grading;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
 * [X] 1. `public` methods must not have any side effects.
 *
 * [X] 2. You may assume that the `apply()` method is passed a `List` that does not contain any `null`
 * elements.
 *
 * [X] 3. The default constructor must construct a `DropFilter` object that drops the lowest and highest
 * element.
 *
 * [X] 4. The `apply()` method must construct a new `List` that is a subset of the `List` it is passed.
 *
 *      [X] 4.1. If the `apply()` method is passed a `List` that has an inappropriate size then it must throw
 *      a `SizeException`.
 *
 *          [X] 4.1.1. If the `apply()` method is passed a `null` `List` then it must throw a `SizeException`.
 *          [X] 4.1.2. If the `apply()` method is passed a `List` that contains fewer elements than are to
 *          be dropped then it must throw a `SizeException`.
 *          [X] 4.1.3. If the `apply()` method is passed a `List` that contains the same number of elements
 *          as are to be dropped then it must throw a `SizeException`.
 *
 *      [X] 4.2. If the `apply()` method is passed a `List` that has an appropriate size then it must return
 *      a new `List`.
 *
 *          [X] 4.2.1. The elements of the new `List` must be aliases for (not copies of) the `Grade` objects
 *          in the `List` it is passed.
 *          [X] 4.2.2. Because each `Grade` object in the `List` has a key that can be used to identify it,
 *          the new `List` need not be in the same order as the `List` it is passed.
 *          [X] 4.2.3. The elements in (and size) of the returned `List` must be based on the values of the
 *          parameters that were passed to the constructor when the object was constructed.
 *
 *              [X] 4.2.3.1. If `shouldDropLowest` was `true` then it must drop exactly one of the elements
 *              with the lowest value in the original `List`.
 *              [X] 4.2.3.2. If `shouldDropHighest` was `true` then it must drop exactly one of the elements
 *              with the highest value in the original `List`.
 *              [X] 4.2.3.3. When dropping the highest and lowest, two elements must always be dropped,
 *              even if the highest and lowest have the same value.
 *              [X] 4.2.3.4. When determining the highest and/or lowest values it must account for missing
 *              (i.e., `null`) values as in the `compareTo()` method of the `Grade` class (i.e., missing
 *              values have smaller magnitude than non-missing values and one missing value has the same
 *              magnitude as another missing value).
 */

public class DropFilter implements Filter
{
    private boolean shouldDropLowest;
    private boolean shouldDropHighest;

    public DropFilter()
    {
        shouldDropLowest = true;
        shouldDropHighest = true;
    }

    public DropFilter(boolean shouldDropLowest, boolean shouldDropHighest)
    {
        this.shouldDropLowest = shouldDropLowest;
        this.shouldDropHighest = shouldDropHighest;
    }

    @Override
    public List<Grade> apply(List<Grade> list) throws SizeException
    {
        if (list == null) throw new SizeException();
        if (shouldDropLowest && shouldDropHighest && list.size() < 3)
            throw new SizeException();
        if ((shouldDropLowest || shouldDropHighest) && list.size() < 2)
            throw new SizeException();
        if (!shouldDropLowest && !shouldDropHighest && list.size() < 1)
            throw new SizeException();

        if (!shouldDropLowest && !shouldDropHighest)
            return list;

        Iterator<Grade> i = list.iterator();
        Grade lowest, highest, g;
        lowest = highest = i.next();

        // Iterate through `list`, find highest and lowest grades
        while (i.hasNext())
        {
            g = i.next();
            if (g.compareTo(lowest) < 0)
                lowest = g;
            if (g.compareTo(highest) > 0)
                highest = g;
        }

        // Make a new copy of `list` to filter and return
        List<Grade> filteredList = new LinkedList<>(list);

        boolean haveDroppedLowest, haveDroppedHighest;
        haveDroppedLowest = haveDroppedHighest = false;

        i = filteredList.iterator();
        // Iterate through again, locate each and drop them
        while (i.hasNext())
        {
            g = i.next();
            if (shouldDropLowest && !haveDroppedLowest && g.compareTo(lowest) == 0)
            {
                i.remove();
                haveDroppedLowest = true;
            }
            else if (shouldDropHighest && !haveDroppedHighest && g.compareTo(highest) == 0)
            {
                i.remove();
                haveDroppedHighest = true;
            }
        }

        return filteredList;
    }
}
