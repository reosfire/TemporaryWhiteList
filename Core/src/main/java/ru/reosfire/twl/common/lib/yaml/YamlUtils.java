package ru.reosfire.twl.common.lib.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlUtils {
    private static final Object lock = new Object();
    private static final Yaml shared = createDefaultYaml();

    public static Yaml createDefaultYaml() {
        DumperOptions dumperOptions = new DumperOptions();

        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setProcessComments(true);

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setProcessComments(true);

        return new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
    }
    public static Map<String, Object> loadYaml(File file) {
        synchronized (lock) {
            try (InputStream stream = new FileInputStream(file)) {
                return shared.load(stream);
            }
            catch (Exception e) {
                throw new RuntimeException("Error while loading yaml", e);
            }
        }
    }

    public static boolean mergeYaml(Node reference, Node receiver) {
        if (reference == null) throw new IllegalArgumentException("Reference yaml should not be null");
        if (receiver == null) throw new IllegalArgumentException("Receiver yaml should not be null");
        if (reference.getClass() != receiver.getClass()) throw new IllegalArgumentException("Reference and receiver should be of same type");

        if (reference instanceof MappingNode) return mergeMappings((MappingNode) reference, (MappingNode) receiver);
        else if (reference instanceof SequenceNode) return mergeSequences((SequenceNode) reference, (SequenceNode) receiver);
        else throw new RuntimeException("Unsupported node type");
    }

    public static boolean mergeMappings(MappingNode reference, MappingNode receiver) {
        boolean result = false;

        Map<String, NodeTuple> recMap = toMap(receiver.getValue());

        for (NodeTuple nodeTuple : reference.getValue()) {
            String key = ((ScalarNode)nodeTuple.getKeyNode()).getValue();
            Node value = nodeTuple.getValueNode();

            NodeTuple recChild = recMap.get(key);

            if (recChild == null) {
                receiver.getValue().add(nodeTuple);
                result = true;
            }
            else if (recChild.getValueNode().getClass() != value.getClass()) {
                int index = receiver.getValue().indexOf(recChild);
                receiver.getValue().set(index, nodeTuple);
                result = true;
            }
            else {
                if (value instanceof MappingNode) {
                    result |= mergeMappings((MappingNode) value, (MappingNode) recChild.getValueNode());
                }
                else if (value instanceof SequenceNode) {
                    result |= mergeSequences((SequenceNode) value, (SequenceNode) recChild.getValueNode());
                }
            }
        }

        return result;
    }

    private static Map<String, NodeTuple> toMap(List<NodeTuple> nodes) {
        Map<String, NodeTuple> result = new LinkedHashMap<>();

        for (NodeTuple node : nodes) {
            String key = ((ScalarNode)node.getKeyNode()).getValue();
            result.put(key, node);
        }

        return result;
    }

    private static boolean mergeSequences(SequenceNode reference, SequenceNode receiver) {
        boolean added = false;
        for (Node newNode : reference.getValue()) {
            if (!contains(receiver.getValue(), newNode)) {
                receiver.getValue().add(newNode);
                added = true;
            }
        }

        return added;
    }

    private static boolean contains(List<Node> nodes, Node node) {
        for (Node i : nodes) {
            if (areEquals(i, node)) return true;
        }
        return false;
    }

    private static boolean areEquals(Node first, Node second) {
        if (first == null && second == null) return true;
        if (first == null || second == null) return false;
        if (first.getClass() != second.getClass()) return false;

        if (first instanceof MappingNode) {
            List<NodeTuple> f = ((MappingNode) first).getValue();
            List<NodeTuple> s = ((MappingNode) second).getValue();

            if (f.size() != s.size()) return false;

            boolean allEquals = true;

            for (int i = 0; i < f.size(); i++) {
                allEquals &= areEquals(f.get(i).getKeyNode(), s.get(i).getKeyNode());
                allEquals &= areEquals(f.get(i).getValueNode(), s.get(i).getValueNode());
            }

            return allEquals;
        }
        else if (first instanceof SequenceNode) {
            List<Node> f = ((SequenceNode) first).getValue();
            List<Node> s = ((SequenceNode) second).getValue();

            if (f.size() != s.size()) return false;

            boolean allEquals = true;

            for (int i = 0; i < f.size(); i++) {
                allEquals &= areEquals(f.get(i), s.get(i));
            }

            return allEquals;
        }
        else if (first instanceof ScalarNode) return true;
        else throw new RuntimeException("Unknown node type reached");
    }
}