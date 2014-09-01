// Copyright (c) 2014 K Team. All Rights Reserved.
package org.kframework.backend.java.indexing;

import java.util.List;

import org.kframework.backend.java.kil.Cell;
import org.kframework.backend.java.kil.Definition;
import org.kframework.backend.java.kil.Kind;
import org.kframework.backend.java.kil.Term;
import org.kframework.backend.java.symbolic.BottomUpVisitor;
import org.kframework.kil.Attribute;
import org.kframework.kil.loader.Constants;

import com.google.common.collect.Lists;

/**
 * Collects indexing cells used in {@link IndexingTable}.
 *
 * @author YilongL
 *
 */
public class IndexingCellsCollector extends BottomUpVisitor {

    private final Definition definition;
    private final List<Cell<?>> indexingCells;

    public static List<Cell<?>> getIndexingCells(Term term, Definition definition) {
        IndexingCellsCollector collector = new IndexingCellsCollector(definition);
        term.accept(collector);
        return collector.indexingCells;
    }

    private IndexingCellsCollector(Definition definition) {
        this.definition = definition;
        this.indexingCells = Lists.newArrayList();
    }

    @Override
    public void visit(Cell cell) {
        String cellLabel = cell.getLabel();
        String streamCellAttr = definition.context()
                .getConfigurationStructureMap().get(cellLabel).cell
                .getCellAttribute(Attribute.STREAM_KEY);

        if (cellLabel.equals("k")
                || Constants.STDIN.equals(streamCellAttr)
                || Constants.STDOUT.equals(streamCellAttr)
                || Constants.STDERR.equals(streamCellAttr)) {
            indexingCells.add(cell);
        };

        if (cell.contentKind() == Kind.CELL_COLLECTION) {
            super.visit(cell);
        }
    }
}