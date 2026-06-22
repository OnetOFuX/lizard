import json
import time

from kafka import KafkaConsumer


TOPIC_ORDENES = "orden-eventos"
GROUP_ID = "orden-py-group"

consumer = KafkaConsumer(
    TOPIC_ORDENES,
    bootstrap_servers="kafka:9092",
    auto_offset_reset="earliest",
    enable_auto_commit=True,
    group_id=GROUP_ID,
    value_deserializer=lambda value: json.loads(value.decode("utf-8")),
)

print(json.dumps({
    "service": "orden-py",
    "component": "consumer",
    "topic": TOPIC_ORDENES,
    "groupId": GROUP_ID,
    "bootstrapServers": "kafka:9092",
    "status": "listening",
}))

for msg in consumer:
    event = msg.value
    timestamp = event.get("timestamp")
    processed_at = int(time.time() * 1000)
    latency_ms = processed_at - timestamp if timestamp is not None else None
    is_valid = (
        event.get("tipoEvento") is not None
        and event.get("ordenId") is not None
        and event.get("total") is not None
        and timestamp is not None
    )

    log = {
        "service": "orden-py",
        "component": "consumer",
        "topic": msg.topic,
        "partition": msg.partition,
        "offset": msg.offset,
        "groupId": GROUP_ID,
        "eventType": event.get("tipoEvento"),
        "ordenId": event.get("ordenId"),
        "timestamp": timestamp,
        "isValid": is_valid,
        "processedAt": processed_at,
        "latencyMs": latency_ms,
        "status": "consumed" if is_valid else "invalid",
    }

    print(json.dumps(log))
