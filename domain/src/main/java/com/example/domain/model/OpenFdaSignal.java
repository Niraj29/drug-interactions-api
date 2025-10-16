package com.example.domain.model;

import java.util.List;
import java.util.Map;

public record OpenFdaSignal(long count, List<Map.Entry<String, Long>> topReactions) {}
