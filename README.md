# MARSCOLONY

A Java simulation of a Mars colony : crew, robots, facilities, resources, missions and emergencies.

## Main components
- `agents/` - CrewMember (Scientist, Engineer, MedicalOfficer) and Robot (ExplorationRover, RepairRobot, Drone). Both extend `AbstractAgent`.
- `Facilities/` - Habitat, Laboratory, PowerStation, StorageFacilities, CommunicationStation.
- `resources/` - resource types and quantities.
- `missions/` - Exploration, Research, Repair, Rescue (all extend `Mission`).
- `environment/` - Normal, Dust Storm, Communication Disruption.
- `emergencies/` - `EmergencyManager`.
- `colony/MarsColony` - connects everything and runs the time.
- `uml/` - the 5 UML diagrams.

## Design decisions
- Agents are asked what they can do (`Capability`), we don't check their class type.
- Only the colony owns and tracks missions (facilities don't).
- Relationships: Inheritance (CrewMember -> Engineer), Aggregation (colony has crew and robots), Composition (facility owns its resources/equipment), Association (agent works on a mission).

## Resource rules
- Colony total = sum of the resources in all **operational** facilities.
- A mission can take from more than one facility.
- Allocation policy: facilities are used in the order they were added to the colony.
- All-or-nothing: if resources are not enough, nothing is consumed and the mission fails with a reason.

## Mission states
`CREATED -> ASSIGNED -> IN_PROGRESS -> COMPLETED` or `FAILED`.
- Only one active mission at a time.
- A rejected mission becomes FAILED right away and never becomes active.
- Failed missions are not retried (make a new one).
- Agents are released when the mission ends.

## Environment
- Dust storm: surface exploration can't start, and active rover missions fail.
- Communication disruption: communication-dependent missions can't start and drones can't be controlled.
- Missions already in progress: they are checked when the condition starts and at every time unit. If they can't continue, they fail immediately with a reason.

## Emergencies
- Oxygen leak: oxygen is deducted from the affected habitat only (never below 0). It needs 1 engineer + 1 repair robot and some spare parts.
- If the responders or the parts are not available: the emergency is UNRESOLVED, no resources are consumed, and the same oxygen is deducted again (so it costs double).
- Emergencies don't stop the active mission. Responders must be free.


`Main` runs the 5 required scenarios in order: successful exploration, mission rejection, resource shortage, oxygen leak, dust storm.
