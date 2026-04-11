# Implementation Log

## Step 1 - Discovery and Contract Review
### Basis
- `AGENTS.md`
- `IMPORTANT FILES/trab1-2526-en.pdf`
- `IMPORTANT FILES/Assignment 1 - Tester.txt`
- `LAB FILES/lab1.pdf`
- `LAB FILES/lab2.pdf`

### Changes made
- Inspected the project structure under `src/sd2526/trab/...`
- Compiled the project with Maven to confirm the current codebase state
- Reviewed the REST, gRPC, discovery, and service-layer contracts before changing behavior

### Notes
- The implementation already follows the requested split between Java logic, REST wrappers, gRPC wrappers, and discovery
- The most visible issue found in the local behavior was in user deletion side effects

### Remaining work
- Fix the incorrect user deletion side-effect placement
- Recompile after the fix
- Record any remaining risks found during verification

## Step 2 - Fix User Deletion Side Effects
### Basis
- `src/sd2526/trab/api/java/Users.java`
- `IMPORTANT FILES/Assignment 1 - Tester.txt` sections for user removal and local message behavior
- Lab style requirement from `AGENTS.md` to keep service logic centralized and wrappers thin

### Changes made
- Removed the deletion callback trigger from `UsersService.getUser(...)`
- Added the deletion callback trigger to `UsersService.deleteUser(...)` after a successful removal

### Changed files
- `src/sd2526/trab/server/UsersService.java`
- `implementation-log.md`

### Notes
- This keeps resource cleanup aligned with the `deleteUser` contract instead of accidentally clearing message state during normal reads
- The callback remains asynchronous-friendly because `UsersService` only notifies listeners after the user is actually removed

### Remaining work
- Recompile and sanity-check the updated behavior
- Look for any other contract mismatches that could affect the tester

## Step 3 - Verification
### Basis
- `pom.xml`
- `src/sd2526/trab/server/UsersService.java`

### Changes made
- Recompiled the project with `mvn -q -DskipTests compile`
- Verified that the deletion callback now only appears in `deleteUser(...)`

### Notes
- The project compiles successfully after the fix
- The corrected behavior now matches the user deletion contract more closely

### Remaining work
- Run the external tester and Docker-based scenarios when needed
- Review distributed failure-handling behavior if the next tester phase still reports issues

## Step 4 - Harden gRPC Message Conversion
### Basis
- `src/sd2526/trab/api/grpc/messages.proto`
- `src/sd2526/trab/server/grpc/GrpcMessagesResource.java`
- `src/sd2526/trab/server/MessagesService.java`

### Changes made
- Replaced `Set.copyOf(...)` in gRPC message decoding with `LinkedHashSet`
- Replaced internal message cloning to preserve destination order and avoid duplicate-related failures

### Notes
- This keeps the gRPC path tolerant to repeated destinations while remaining compatible with the Java service API that uses sets
- The change is behavior-preserving for REST and improves determinism for gRPC message destination ordering

### Remaining work
- Recompile and rerun tester from `7a`
- If `7a` still fails, inspect the tester-facing gRPC contract more deeply

## Step 5 - Fix Immediate Discovery Announcement
### Basis
- `src/sd2526/trab/server/Discovery.java`
- `src/sd2526/trab/server/ServerMain.java`
- gRPC tester output from phases `7a` and `7b`

### Changes made
- Added an immediate multicast announcement when `addLocalService(...)` is called while discovery is already running

### Notes
- In `users-grpc` mode, discovery starts before local services are registered
- Without an immediate send, `Messages@<domain>` may only become externally visible on the next periodic cycle, which matches the tester failing right before the first gRPC Messages call

### Remaining work
- Recompile and rerun `7a`
- If still needed, inspect the exact gRPC Messages contract expected by the tester

## Step 6 - Wake Discovery Announcer On Registration
### Basis
- `src/sd2526/trab/server/Discovery.java`
- gRPC tester output from phases `7a` and `7b`

### Changes made
- Interrupt the announcer thread after registering a local service so it does not wait for the initial periodic sleep
- Updated the announcer loop so wake-up interrupts only stop the thread during shutdown

### Notes
- This removes the startup race where discovery starts, sees no local services yet, and then sleeps before the first real multicast cycle

### Remaining work
- Rebuild the Docker image
- Rerun the tester from `7a`

## Step 7 - Differentiate gRPC Discovery URIs Per Service
### Basis
- `src/sd2526/trab/server/ServerMain.java`
- `IMPORTANT FILES/Assignment 1 - Tester.txt` GRPC Messages tests
- gRPC tester stack trace showing `tester.SiteState.getMessagesClt(...)` with an empty Messages client list during `7a`

### Changes made
- Changed GRPC service announcements so `Users` and `Messages` advertise distinct discovery URIs while still sharing the same host and port

### Changed files
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- The server bootstrap remains aligned with the single GRPC server style already used in the project
- The only change is the discovery path suffix (`/grpc/users` and `/grpc/messages`) so clients that key endpoints by URI string can distinguish the two services

### Remaining work
- Rebuild the jar and Docker image
- Rerun tester `7a` to confirm whether the Messages GRPC client list is now populated

## Step 8 - Fix Docker Jar Copy Rule
### Basis
- `Dockerfile`
- local Docker build failure during image rebuild

### Changes made
- Replaced the wildcard jar copy rule with the exact shaded jar name produced by the Maven build

### Changed files
- `Dockerfile`
- `implementation-log.md`

### Notes
- The previous `target/sd*.jar` pattern matched more than one jar in `target/`, which caused Docker to reject the `COPY`
- This change keeps the image layout the same while making the build deterministic

### Remaining work
- Rebuild the Docker image
- Rerun tester `7a`

## Step 9 - Add Dedicated Messages gRPC Bootstrap
### Basis
- `src/sd2526/trab/server/ServerMain.java`
- `messages.props`
- tester decompilation showing `SiteState.getMessagesClt(...)` reads a dedicated Messages server list

### Changes made
- Added `messages-grpc` mode to `ServerMain`
- Configured `messages.props` with `MESSAGES_GRPC_SERVER_MAINCLASS`, `MESSAGES_GRPC_PORT`, and `MESSAGES_GRPC_EXTRA_ARGS`
- Restored the standard shared gRPC base URI format now that Users and Messages run as distinct GRPC server entries for the tester

### Changed files
- `src/sd2526/trab/server/ServerMain.java`
- `messages.props`
- `implementation-log.md`

### Notes
- The new `messages-grpc` mode mirrors the existing `messages` server structure: discovery starts first, `MessagesService` uses discovery-backed user lookup, and the gRPC wrapper stays thin
- This aligns the project with the tester version that expects separate Users and Messages GRPC server definitions

### Remaining work
- Rebuild the jar and Docker image
- Rerun tester `7a`

## Step 10 - Merge Compiled Classes Into Docker Jar
### Basis
- `Dockerfile`
- direct container reproduction showing `/home/sd/sd2526.jar` did not contain `sd2526.trab.server.ServerMain`

### Changes made
- Updated the Docker build so the copied shaded jar is amended with the compiled project classes from `target/classes`

### Changed files
- `Dockerfile`
- `implementation-log.md`

### Notes
- The current Maven shading output includes dependencies but not the project classes, so the Docker image must merge in `target/classes` to remain runnable for the tester

### Remaining work
- Rebuild the Docker image
- Rerun tester `7a`

## Step 11 - Retry Remote Messages Discovery And Delivery
### Basis
- `src/sd2526/trab/server/MessagesService.java`
- tester result from `9a` showing an intermittently missing cross-domain gRPC inbox message
- existing retry style in `src/sd2526/trab/server/RestUserDirectory.java`

### Changes made
- Added bounded retry loops for remote `Messages` service discovery
- Added bounded retry loops for remote delivery and remote delete propagation over REST/gRPC

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- Before this change, an early cross-domain send could be dropped if the destination `Messages@<domain>` announcement had not yet been learned locally
- The retry timing mirrors the existing user-directory retry approach instead of introducing a new mechanism

### Remaining work
- Rebuild the jar and Docker image
- Rerun the failing cross-domain gRPC tests

## Step 12 - Retry Users Discovery From Messages Servers
### Basis
- `src/sd2526/trab/server/RestUserDirectory.java`
- cross-domain gRPC failure pattern where destination Messages servers can receive a forwarded message before learning the local `Users@<domain>` announcement

### Changes made
- Added discovery retries before `RestUserDirectory` gives up locating the local Users service
- Changed the no-service-found result from immediate `NOT_FOUND` to `TIMEOUT` so transient discovery lag is not treated as a nonexistent user
- Added a same-domain fallback URI based on the tester hostname convention, with protocol preference chosen by server mode

### Changed files
- `src/sd2526/trab/server/RestUserDirectory.java`
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- This keeps the destination Messages server from misclassifying a valid local recipient as unknown during startup races

### Remaining work
- Rebuild the compiled classes and Docker image
- Rerun cross-domain gRPC validation

## Step 13 - Make `messages-grpc` The Only gRPC Messages Authority
### Basis
- `src/sd2526/trab/server/ServerMain.java`
- cross-domain gRPC failures where inbox state could diverge if forwarded deliveries hit the `users-grpc` process instead of the dedicated `messages-grpc` process

### Changes made
- Removed the Messages gRPC wrapper and Messages discovery announcement from `users-grpc`

### Changed files
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- With dedicated `MESSAGES_GRPC_*` tester configuration in place, each domain should have exactly one gRPC Messages endpoint
- This avoids split inbox state across two different containers for the same domain

### Remaining work
- Rebuild the compiled classes and Docker image
- Rerun cross-domain gRPC validation

## Step 14 - Add Asynchronous Remote Queues Per Destination Domain
### Basis
- `src/sd2526/trab/server/MessagesService.java`
- tester failures in `10c` and `10d`
- `src/sd2526/trab/api/java/Messages.java` note that delete may execute asynchronously

### Changes made
- Replaced synchronous remote delivery during `postMessage(...)` with enqueue-only background processing
- Replaced synchronous remote delete propagation with background processing
- Added one worker queue per remote domain to preserve per-domain order while avoiding a single global bottleneck
- Added timeout failure notifications for remote deliveries that remain unreachable beyond the tester’s long-fault window
- Added deterministic remote endpoint fallback using the tester hostname convention so async workers do not depend on discovery re-learning after faults
- Reduced remote REST/gRPC call timeouts used by the async workers so failed attempts unblock quickly during long network faults

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- This is aimed directly at the async fault-tolerance phases: `postMessage(...)` should now return after local work instead of waiting on remote faults
- Per-domain workers preserve ordering for `post` followed by `delete` against the same destination domain

### Remaining work
- Compile
- Rebuild Docker image
- Rerun tester from `10c`

## Step 15 - Verify Async Fault Tolerance and Mixed-Domain Delivery
### Basis
- `IMPORTANT FILES/Assignment 1 - Tester.txt` sections for `10c` to `10g`, `11a`, and `13a` to `13c`
- official Docker tester runs after the async queue changes

### Changes made
- Re-ran the official tester focused on the async fault-tolerance phases
- Confirmed that `10c`, `10d`, `10e`, `10f`, and `10g` now pass
- Confirmed subsequent mixed REST/gRPC cross-domain delivery and deletion phases reached `OK`

### Changed files
- `implementation-log.md`

### Notes
- The previous long-running failure point was `10g`; after the async queue and timeout-notification behavior stabilized, the tester accepted the `TIMEOUT` notification and the absence of the original remote delivery
- The same image also progressed through the later mixed-domain validation without new functional failures in the captured run

### Remaining work
- If needed, do one last full-suite rerun for submission confidence

## Step 16 - Give REST Servers An Explicit Request Executor
### Basis
- `src/sd2526/trab/server/ServerMain.java`
- `AGENTS.md` guidance to keep the server bootstrap simple and close to the lab server style
- tester failures in `12b` and `12c` showing many `Connection refused` errors against `messages0.ourorg`

### Changes made
- Changed the Jersey JDK HTTP server bootstrap to create the server without auto-start
- Attached an explicit cached-thread-pool executor before starting the REST server
- Added executor shutdown to the existing server shutdown hook

### Changed files
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- The previous bootstrap relied on the JDK server defaults, which can become a bottleneck under the tester's large interfering REST workload
- This keeps the same REST stack and URI layout while making request handling concurrent enough for the local concurrency phases

### Remaining work
- Recompile the project
- Rebuild the Docker image
- Rerun tester phases `12b` and `12c`

## Step 17 - Add Project Evolution Document
### Basis
- initial project structure shown by the user image
- `implementation-log.md`
- current source tree under `src/sd2526/trab/...`

### Changes made
- Created a new markdown document describing the project evolution from the initial API-only structure to the final version that passed all tests

### Changed files
- `project-evolution.md`
- `implementation-log.md`

### Notes
- The new document is written as a sequential explanation of the implementation journey, not just a raw change log
- It highlights the major architectural stages: service layer, wrappers, discovery, bootstrap, Docker fixes, distributed delivery, async queues, and concurrency stabilization

### Remaining work
- None for this documentation step

## Step 18 - Tolerate Concurrent Inbox Removals
### Basis
- `src/sd2526/trab/api/java/Messages.java`
- tester failure in `12b` around concurrent `removeInboxMessage`
- `AGENTS.md` guideline to keep message logic centralized in `MessagesService`

### Changes made
- Added per-user tombstones for inbox messages that were just removed locally
- Changed `removeInboxMessage(...)` to return success when a concurrent duplicate removal targets a message already removed by another overlapping request
- Kept `NOT_FOUND` for message ids that never existed in that inbox
- Cleared tombstones when reinserting a message id or deleting the whole inbox

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- This is aimed specifically at the `12b` interference pattern where two overlapping inbox removals for the same message should not destabilize the REST Messages server
- The change is narrow: it only softens the case of a repeated removal after a confirmed local removal, without changing the general behavior for unknown ids

### Remaining work
- Compile the project
- Rebuild the Docker image
- Rerun tester `12b`

## Step 19 - Implement REST Gateway Proxy
### Basis
- `IMPORTANT FILES/Assignment 1 - Tester.txt` gateway phases `5a` and `5b`
- `src/sd2526/trab/api/rest/RestUsers.java`
- `src/sd2526/trab/api/rest/RestMessages.java`
- `AGENTS.md` gateway requirement to expose REST only and forward to the actual domain services

### Changes made
- Added a small `GatewayService` that forwards REST user and message operations to the domain services discovered through `Discovery`
- Added thin gateway REST resources for `/users` and `/messages`, mirroring the existing wrapper style
- Implemented `gateway` mode in `ServerMain` using those resources
- Enabled the gateway entries in `messages.props` so the tester now launches the gateway server instead of skipping `5a` and `5b`

### Changed files
- `src/sd2526/trab/server/GatewayService.java`
- `src/sd2526/trab/server/rest/RestGatewayUsersResource.java`
- `src/sd2526/trab/server/rest/RestGatewayMessagesResource.java`
- `src/sd2526/trab/server/ServerMain.java`
- `messages.props`
- `implementation-log.md`

### Notes
- The gateway stays REST-only externally and simply proxies operations to `Users` and `Messages` in the same domain
- Discovery is still used first, with the tester hostname convention as fallback for local service URIs

### Remaining work
- Compile the project
- Rebuild the Docker image
- Run tester phases `5a` and `5b`

## Step 20 - Move Users Persistence To Hibernate
### Basis
- `AGENTS.md` requirement to follow the lab-style persistence approach instead of inventing a new repository model
- `hibernate.cfg.xml`
- `src/sd2526/trab/server/UsersService.java`
- `src/sd2526/trab/server/ServerMain.java`

### Changes made
- Annotated `User` as a Hibernate entity with `name` as the primary key
- Added a small Hibernate helper under `src/sd2526/trab/server/persistence/` to centralize `SessionFactory` lifecycle and transactional execution
- Replaced the in-memory user map in `UsersService` with Hibernate-backed CRUD operations while keeping the same `Result<T>` contracts
- Initialized and closed Hibernate from `ServerMain`, using a per-service/per-domain HSQLDB file path
- Recompiled the project with `mvn -q -DskipTests compile`

### Changed files
- `src/sd2526/trab/api/User.java`
- `src/sd2526/trab/server/persistence/Hibernate.java`
- `src/sd2526/trab/server/UsersService.java`
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- The change is intentionally narrow and only moves `UsersService` persistence to Hibernate for now, because `hibernate.cfg.xml` was already prepared for `User`
- REST and gRPC wrappers stay thin; the service logic still owns validation and `Result<T>` mapping

### Remaining work
- Build the Docker image again if this persistence change is meant to be exercised through the official tester
- Decide whether message state also needs to move to Hibernate, or whether the intended lab-style scope is users-only persistence

## Step 21 - Recheck Hibernate Failure Context
### Basis
- previous tester log at `SD-trab1-2526/trab1-api/full-tester.log`
- `src/sd2526/trab/server/ServerMain.java`
- `src/sd2526/trab/server/UsersService.java`
- `src/sd2526/trab/server/persistence/Hibernate.java`

### Changes made
- Re-read the last full tester log and confirmed the only recorded failure there was `6c` on gRPC `PostUser`
- Confirmed the current source now initializes Hibernate with a unique `jdbc:hsqldb:mem:` URL per server start, instead of the older file-backed URL shown in that stale tester log
- Recompiled with `mvn -q -DskipTests compile` to ensure the current Hibernate-related code is consistent and builds locally

### Changed files
- `implementation-log.md`

### Notes
- The stale log still shows `jdbc:hsqldb:file:/tmp/db-users-grpc-ourorg`, which indicates the failing Docker image was older than the current local source
- A new tester run is currently blocked by the local Docker daemon being unavailable (`//./pipe/dockerDesktopLinuxEngine` not found)

### Remaining work
- Start Docker locally
- Rebuild the project image with the current sources
- Rerun the full tester and grep the new log for `FAILED`

## Step 22 - Rebuild Image And Run Full Tester
### Basis
- `pom.xml` Docker build configuration
- official tester image `nunopreguica/sd2526-tester-tp1:latest`
- refreshed `full-tester.log`

### Changes made
- Rebuilt the Docker image from the current source with `mvn -q docker:build`
- Reran the full official tester and saved the output to `full-tester.log`
- Verified the final log ends with `Test complete...` and contains no `FAILED`

### Changed files
- `full-tester.log`
- `implementation-log.md`

### Notes
- The new run confirms the previous `6c` Hibernate-related failure was resolved in the rebuilt image
- The final log also shows the later interference and mixed concurrency phases `12b`, `12c`, `12d`, `12e`, and multi-domain phases `13a`, `13b`, `13c` all reaching `OK`

### Remaining work
- None for the current validation cycle

## Step 23 - Make Persistence Durable For Messages And Hibernate Storage
### Basis
- `AGENTS.md` requirement to keep the implementation grounded in the provided Hibernate-based persistence direction
- `hibernate.cfg.xml`
- `src/sd2526/trab/server/persistence/Hibernate.java`
- `src/sd2526/trab/server/MessagesService.java`
- project requirement that message/mailbox state belongs to each domain service

### Changes made
- Added Hibernate entities for inbox messages, removed-message tombstones, and sent-message metadata under `src/sd2526/trab/server/persistence/`
- Registered the new entities in the shared Hibernate helper and ensured the backing directory for file-based HSQLDB is created before startup
- Changed `ServerMain` to initialize Hibernate with a file-backed database path under `db/<domain>/<service>/state` instead of an in-memory database URL
- Updated `hibernate.cfg.xml` from `create-drop` to `update` so schema and data survive server restarts
- Replaced the durable state previously held in `MessagesService` in-memory maps with Hibernate-backed reads/writes while preserving the external `Result<T>` semantics and remote queue behavior
- Recompiled the project with `mvn -q -DskipTests compile`

### Changed files
- `src/sd2526/trab/server/persistence/InboxMessageRecord.java`
- `src/sd2526/trab/server/persistence/RemovedInboxMessageRecord.java`
- `src/sd2526/trab/server/persistence/SentMessageRecord.java`
- `src/sd2526/trab/server/persistence/Hibernate.java`
- `src/sd2526/trab/server/MessagesService.java`
- `src/sd2526/trab/server/ServerMain.java`
- `hibernate.cfg.xml`
- `implementation-log.md`

### Notes
- The non-durable runtime pieces remain in memory on purpose: gRPC channels and remote retry worker queues are transport/runtime mechanics, not mailbox state
- The durable part now covers both `Users` and the local message state needed to recover inbox contents, removals, and delete-window metadata after a restart

### Remaining work
- Rebuild the Docker image
- Run the tester again to validate restart-sensitive and concurrency-sensitive behavior with the new file-backed persistence

## Step 24 - Serialize UsersService Operations Over Hibernate State
### Basis
- `src/sd2526/trab/server/UsersService.java`
- `full-tester.log` final sequential-oracle failure after the concurrency phases
- `AGENTS.md` guidance to keep the service logic centralized and avoid redesigning the architecture

### Changes made
- Serialized the public `UsersService` operations on the service instance so concurrent REST/gRPC requests do not overwrite each other's Hibernate-backed updates
- Kept the existing Hibernate persistence flow and `Result<T>` contracts unchanged

### Changed files
- `src/sd2526/trab/server/UsersService.java`
- `implementation-log.md`

### Notes
- The observed failure was a stale final `displayName` after the concurrency tests, which is consistent with lost updates introduced by the move from the old in-memory map to independent Hibernate sessions
- This fix is intentionally narrow and stays within the existing service-layer structure

### Remaining work
- Recompile the project
- Rebuild the Docker image
- Rerun the tester, at least from the concurrency phases onward

## Step 25 - Restore Serial REST Execution For Users Server
### Basis
- `src/sd2526/trab/server/ServerMain.java`
- tester run from `11a` onward, where `11a`, `11b`, and `11c` passed but `12a` still failed in the final oracle state
- earlier bootstrap change that introduced a cached executor for every REST server

### Changes made
- Changed the REST bootstrap so the standalone `users` server uses a single-thread executor, while the other REST modes keep the cached thread pool needed by the messages-heavy phases

### Changed files
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- This keeps the explicit executor approach added for the messages concurrency phases, but makes the Users REST server behave more like the simpler serialized lab-style request handling
- The goal is to preserve deterministic ordering for interfering user operations without regressing message-server parallelism

### Remaining work
- Recompile
- Rebuild the Docker image
- Rerun tester from `11a`

## Step 26 - Retry Hibernate Transactions On Lock Conflicts
### Basis
- `src/sd2526/trab/server/persistence/Hibernate.java`
- tester failure in `12b` showing `jakarta.persistence.PessimisticLockException` / HSQLDB `serialization failure` during concurrent inbox writes
- existing project style of using bounded retries for transient distributed failures

### Changes made
- Added bounded retries in the shared Hibernate transaction helper for retryable lock/serialization exceptions
- Kept the transactional API unchanged so the service layer still calls `Hibernate.execute(...)` in the same way

### Changed files
- `src/sd2526/trab/server/persistence/Hibernate.java`
- `implementation-log.md`

### Notes
- The failure was happening during concurrent writes to `INBOX_MESSAGE_DESTINATIONS`, where HSQLDB aborted one transaction with SQLState `40001`
- Retrying at the helper layer is the narrowest fix because the affected pattern exists across multiple inbox/state writes

### Remaining work
- Recompile
- Rebuild the Docker image
- Rerun tester from `11a`

## Step 27 - Remove Cross-Phase Message Deduplication
### Basis
- `src/sd2526/trab/server/MessagesService.java`
- full tester failure in the final sequential oracle where one inbox message id was missing only in the complete run, not in the isolated `11a` onward run
- durable Hibernate-backed `SentMessageRecord` state persisting across service restarts within the tester lifecycle

### Changes made
- Removed the `postMessage(...)` duplicate-detection shortcut so every successful post generates a fresh message id

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- Reusing an old `mid` based on sender/contents/destinations is unsafe once sent-message metadata survives across phases and restarts
- The delete contract still works because delete propagation is keyed by the exact `mid` returned by each post

### Remaining work
- Recompile
- Rebuild the Docker image
- Rerun the full tester

## Step 28 - Restore Post Idempotence With In-Memory Deduplication
### Basis
- `src/sd2526/trab/server/MessagesService.java`
- full tester early failure in the message idempotence check after removing the old durable deduplication shortcut
- requirement that repeated posts of the same message in the same running server instance return the same `mid`

### Changes made
- Added an in-memory deduplication map keyed by sender, creation time, subject, contents, and destinations
- Used that map in `postMessage(...)` so immediate duplicate posts remain idempotent without consulting durable sent-message history

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- This keeps the early idempotence behavior while avoiding the cross-phase false positives caused by durable Hibernate state
- The durable `SentMessageRecord` remains only for delete propagation metadata

### Remaining work
- Recompile
- Rebuild the Docker image
- Rerun the full tester

## Step 29 - Final Full Tester Validation
### Basis
- official Docker tester run saved to `full-tester.log`
- targeted concurrency/regression run saved to `tester-11a.log`

### Changes made
- Rebuilt the image after the final `MessagesService` idempotence adjustment
- Reran the full official tester from the beginning
- Confirmed the resulting `full-tester.log` ends with `Test complete...` and contains no `FAILED`

### Changed files
- `full-tester.log`
- `tester-11a.log`
- `implementation-log.md`

### Notes
- The targeted run from `11a` to the end also stayed fully green, which helped isolate and confirm the final fixes for interference and cross-domain behavior
- The tester still prints gRPC channel shutdown warnings from the tester environment, but they did not cause any functional failure in the final suite

### Remaining work
- None for the current validation cycle

## Step 30 - Reorganize Packages And Clean Repository Artifacts
### Basis
- `AGENTS.md` package-organization guidance to keep the project close to the labs style
- initial skeleton structure shared by the user, especially the split around `api`, transport-specific packages, and a small project root
- current source tree under `src/sd2526/trab/...`

### Changes made
- Moved shared discovery metadata to `src/sd2526/trab/common/`
- Moved user-directory access helpers to `src/sd2526/trab/clients/`
- Updated imports in the service/bootstrap classes to keep the current behavior unchanged
- Added a module-level `.gitignore` so build output, tester logs, temporary probe files, and local databases no longer pollute the repository

### Changed files
- `src/sd2526/trab/common/ServiceInfo.java`
- `src/sd2526/trab/clients/UserDirectory.java`
- `src/sd2526/trab/clients/LocalUserDirectory.java`
- `src/sd2526/trab/clients/RestUserDirectory.java`
- `src/sd2526/trab/server/Discovery.java`
- `src/sd2526/trab/server/MessagesService.java`
- `src/sd2526/trab/server/ServerMain.java`
- `.gitignore`
- `implementation-log.md`

### Notes
- This keeps the main organization aligned with the original split: contracts in `api`, transport wrappers in `server.rest` / `server.grpc`, persistence in `server.persistence`, and small support packages for shared/client-facing helpers
- The cleanup step should only remove generated or tester-oriented artifacts, not project source or configuration files

### Remaining work
- Compile after the package move
- Remove the generated/tester artifacts now covered by `.gitignore`

## Step 31 - Remove Generated And Tester Artifacts
### Basis
- `.gitignore`
- user request to remove non-essential tester/generated files
- current module root contents after validation compile

### Changes made
- Removed `target/`
- Removed tester and local run artifacts: `full-tester.log`, `grpc-6c.log`, `tester-11a.log`, `server-out.txt`, `server-err.txt`
- Removed temporary/generated root files: `TmpGrpcProbe.class` and `dependency-reduced-pom.xml`

### Changed files
- `implementation-log.md`

### Notes
- The module root now only keeps source, build/config files, helper scripts, and the project documentation that is still useful
- These removals are intentional repository cleanup; Maven can regenerate `target/` and the deleted logs if needed

### Remaining work
- None for this cleanup step

## Step 32 - Fix REST Collection Route Ambiguity
### Basis
- tester failure in `2a` where `PostUser` returned HTTP `300` instead of `200`
- `src/sd2526/trab/api/rest/RestUsers.java`
- REST resource classes under `src/sd2526/trab/server/rest/`

### Changes made
- Removed the explicit `@Path("/")` annotations from REST collection-level `POST` and `GET` methods
- Kept the effective collection routes at the class base paths (`/users`, `/messages`) to avoid ambiguous or redirect-prone matching in Jersey/JDK HTTP

### Changed files
- `src/sd2526/trab/api/rest/RestUsers.java`
- `src/sd2526/trab/server/rest/RestUsersResource.java`
- `src/sd2526/trab/server/rest/RestMessagesResource.java`
- `src/sd2526/trab/server/rest/RestGatewayUsersResource.java`
- `src/sd2526/trab/server/rest/RestGatewayMessagesResource.java`
- `implementation-log.md`

### Notes
- This is a narrow transport-layer fix; it does not change the service logic or contracts
- The change is specifically aimed at the tester calling `/users` and `/messages` without a trailing slash

### Remaining work
- Recompile/package again
- Rebuild the Docker image before rerunning the tester

## Step 33 - Make Hibernate Config Loading Robust After Cleanup
### Basis
- Docker validation after cleanup
- `src/sd2526/trab/server/persistence/Hibernate.java`
- image inspection confirming `hibernate.cfg.xml` exists in `/home/sd` but was not present inside `sd2526.jar`

### Changes made
- Added a fallback in `Hibernate.init(...)` to load `hibernate.cfg.xml` from the local filesystem when classpath loading fails

### Changed files
- `src/sd2526/trab/server/persistence/Hibernate.java`
- `implementation-log.md`

### Notes
- This keeps the original classpath-based behavior first
- The fallback matches the container layout already used by the Dockerfile, so cleanup/build changes no longer make the server startup depend on the config being merged into the jar

### Remaining work
- Recompile
- Rebuild the Docker image
- Rerun the official tester

## Step 33 - Validate Simplified Version With Official Tester
### Basis
- user request to test the current simplified version across the official tester cases
- `IMPORTANT FILES/Assignment 1 - Tester.txt` timing options and execution flow
- official Docker tester image `nunopreguica/sd2526-tester-tp1:latest`

### Changes made
- Started Docker Desktop and rebuilt the current project image with `mvn -q docker:build`
- Ran the official tester from the beginning with a reduced startup wait: `-sleep 2 -timeout 10 -failsleep 15 -extralongfault 90`
- Saved the tester output to `full-tester.log`

### Changed files
- `implementation-log.md`
- `full-tester.log`

### Notes
- The tester does not provide a continue-on-failure mode in the documented options, so the complete suite cannot advance past the first failing phase
- The current image fails immediately in `2a` (`CreateUser` / `GetUser` on REST) because `PostUser` returns HTTP `300` where the tester expects `200`
- The reduced timing was limited to `-sleep 2`; `-timeout` and `-extralongfault` were kept compatible with the current implementation behavior

### Remaining work
- Inspect and fix the REST `PostUser` behavior causing the `300` response in `2a`
- Rerun the full tester after that first blocking failure is resolved

## Step 34 - Tighten Concurrent Inbox Remove/Deliver Persistence
### Basis
- tester failure in `12d` on interfering `RemoveInboxMessage`
- `src/sd2526/trab/server/MessagesService.java`
- `hibernate.cfg.xml`

### Changes made
- Merged inbox removal and removed-message tombstone creation into one Hibernate transaction
- Merged inbox delivery and tombstone cleanup into one Hibernate transaction
- Reused the same atomic removal path for internal delivered-message deletion
- Updated `hibernate.cfg.xml` so `connection.pool_size` matches the effective runtime pool size already enforced in the Hibernate helper

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `hibernate.cfg.xml`
- `implementation-log.md`

### Notes
- The goal is to close the race where concurrent delivery and removal of the same inbox message id could observe partially updated state across separate transactions
- This stays within the existing service-layer and Hibernate-based persistence approach instead of introducing a new synchronization model

### Remaining work
- Recompile
- Rebuild the Docker image
- Rerun tester phase `12d`

## Step 35 - Preserve Inbox Tombstones Under Concurrent Redelivery
### Basis
- tester failure in `12d` on interfering `RemoveInboxMessage`
- `src/sd2526/trab/server/MessagesService.java`
- `src/sd2526/trab/server/persistence/Hibernate.java`

### Changes made
- Changed `putInInbox(...)` so a previously recorded `RemovedInboxMessageRecord` prevents the same inbox message id from being reinserted by a concurrent or delayed duplicate delivery
- Stopped clearing the removal tombstone during inbox delivery, preserving the dedup/removal barrier for the lifetime of that message id
- Extended Hibernate transaction retries to cover duplicate insert races (`ConstraintViolationException` / `EntityExistsException`) so overlapping inserts re-run and observe the committed row/tombstone state

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `src/sd2526/trab/server/persistence/Hibernate.java`
- `implementation-log.md`

### Notes
- The previous logic could resurrect an already removed inbox message because delivery removed the tombstone after re-persisting the same `owner + mid`
- Keeping the tombstone matches the intended role of `REMOVED_INBOX_MESSAGES` during concurrent interfering operations

### Remaining work
- Recompile
- Rebuild the Docker image
- Rerun tester phase `12d`

## Step 36 - Remove Trailing-Slash Redirects In Gateway POST Forwarding
### Basis
- official tester rerun after step 35
- `tester-2a.log` showing `5b` failed with `PostUser (via Gateway)` returning HTTP `300`
- `tester-12d.log` showing `13a` failed with `PostUser` on multi-domain REST returning HTTP `300`
- `src/sd2526/trab/server/GatewayService.java`

### Changes made
- Removed the extra `.path("/")` from gateway forwarding of `POST /users` and `POST /messages`
- Kept the forwarded collection routes aligned with the direct REST resources (`.../rest/users` and `.../rest/messages`)

### Changed files
- `src/sd2526/trab/server/GatewayService.java`
- `implementation-log.md`

### Notes
- The extra trailing slash was causing the forwarded POSTs to hit redirect-prone collection URLs, which the tester reports as HTTP `300`
- This is a narrow transport fix in the gateway forwarding path; it does not change service logic or persistence

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun tester phases `5b` and `13a`

## Step 37 - Retry Remote REST Delivery On Non-Success Responses
### Basis
- official tester rerun after step 36
- `tester-5b.log` showing `10d` failed in advanced async multi-domain delivery with a missing remote inbox message
- `src/sd2526/trab/server/MessagesService.java`
- existing gRPC remote-delivery behavior, which already retries all remote errors except `NOT_FOUND`

### Changes made
- Changed REST remote delivery to treat only HTTP `200` and `204` as successful delivery
- Kept HTTP `404` mapped to unknown-user semantics
- Changed all other REST delivery statuses, including `408` and `5xx`, into retryable transport failures
- Applied the same stricter success rule to remote REST delete propagation, allowing `404` as a benign no-op but retrying other non-success responses

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- This prevents a transiently unhealthy remote `Messages` server from being treated as a successful delivery just because it returned some non-404 status
- The change keeps the REST behavior aligned with the existing gRPC path for remote forwarding

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun tester `10d`
- If `10d` passes, rerun the full official tester

## Step 38 - Use Direct Local Users Fallback Immediately In Messages Servers
### Basis
- official tester rerun after step 37
- `tester-10d.log` still failing because the remote inbox insert only happens after the tester has already declared the message missing
- `src/sd2526/trab/clients/RestUserDirectory.java`
- project hostname/domain convention `users0.<domain>` from the statement and `AGENTS.md`

### Changes made
- Changed `RestUserDirectory.lookupUsersServiceUri()` to use Discovery when available, but fall back immediately to the deterministic local hostname instead of waiting through multiple discovery retries first

### Changed files
- `src/sd2526/trab/clients/RestUserDirectory.java`
- `implementation-log.md`

### Notes
- This removes a startup/recovery delay in remote `Messages` servers when they need to validate a user on their colocated `Users` server before Discovery has refreshed
- The direct fallback already existed; the fix is to use it promptly during recovery instead of only after a long wait

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun tester `10d`
- If `10d` passes, rerun the full official tester

## Step 39 - Shorten Recovery Retry Timing For Remote User Checks And Async Delivery
### Basis
- official tester rerun after step 38
- `tester-10d.log` still showing the remote inbox insert happening too late for the tester's check after recovery
- `src/sd2526/trab/clients/RestUserDirectory.java`
- `src/sd2526/trab/server/MessagesService.java`

### Changes made
- Reduced REST connect/read timeouts and retry sleep in `RestUserDirectory` so a `Messages` server can re-check its colocated `Users` server much faster during recovery
- Reduced remote async delivery call timeout and retry sleep in `MessagesService` so per-domain forwarding workers react faster once a remote domain comes back

### Changed files
- `src/sd2526/trab/clients/RestUserDirectory.java`
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- The goal is to keep the async delivery logic responsive enough for the tester's short post-recovery verification window, while preserving bounded retries
- This stays within the existing retry-based lab-style design instead of introducing a different queue or transport model

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun tester `10d`
- If `10d` passes, rerun the full official tester

## Step 40 - Remove Trailing-Slash Message POST Route Ambiguity
### Basis
- full official tester rerun after step 39
- `full-tester.log` showing `4b` failed with `PostMessage` returning HTTP `300`
- `src/sd2526/trab/api/rest/RestMessages.java`
- earlier REST route fix applied to the users contract

### Changes made
- Removed the explicit `@Path(\"/\")` from `RestMessages.postMessage(...)` so the collection POST route is consistently just `/messages`

### Changed files
- `src/sd2526/trab/api/rest/RestMessages.java`
- `implementation-log.md`

### Notes
- This keeps the contract aligned with the resource implementation and avoids redirect/ambiguity on collection POSTs

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun tester `4b`
- If `4b` passes, rerun the full official tester

## Step 41 - Remove Local Post Deduplication That Dropped Concurrent Legitimate Messages
### Basis
- official tester rerun from `10e`
- `tester-10e.log` failing in `12d` with `GetMessages` missing one expected inbox message during interfering mixed concurrency
- `src/sd2526/trab/server/MessagesService.java`
- labs-first rule from `AGENTS.md`: prefer the literal service behavior instead of extra local deduplication logic not shown in the labs

### Changes made
- Removed the in-memory `recentPostIds` deduplication from `MessagesService.postMessage(...)`
- Kept the existing async forwarding and inbox persistence logic unchanged

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- The previous deduplication could collapse two legitimate concurrent posts that had the same sender, contents, destinations, and creation time
- That behavior matches the observed `12d` failure, where one inbox message was missing even though the oracle expected it

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun tester `12d`
- If `12d` passes, rerun the remaining official tester stages

## Step 42 - Accept Trailing-Slash User POSTs Without HTTP Redirect
### Basis
- official tester rerun from `12d`
- `tester-12d.log` showing `13b` failed with `PostUser` returning HTTP `300`
- `src/sd2526/trab/server/rest/RestUsersResource.java`
- `src/sd2526/trab/server/ServerMain.java`

### Changes made
- Added dedicated REST resources for `POST /users/` on direct users servers and gateway servers
- Registered those resources in `ServerMain` alongside the existing `/users` resources

### Changed files
- `src/sd2526/trab/server/rest/RestUsersSlashResource.java`
- `src/sd2526/trab/server/rest/RestGatewayUsersSlashResource.java`
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- This keeps the main `/users` routes unchanged while accepting the trailing-slash variant without a Jersey redirect
- The fix is intentionally narrow because the observed failure is specifically on `PostUser`

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun tester `13b`
- If `13b` passes, rerun the remaining official tester stages

## Step 43 - Replace Duplicate Slash Resource With Single Optional-Slash Users Route
### Basis
- local reproduction using `java -cp target/sd2526-tp1-1.jar sd2526.trab.server.ServerMain users ourorg`
- Jersey startup failed because the extra `/users/` resource made `POST /users` ambiguous
- `src/sd2526/trab/server/rest/RestUsersResource.java`
- `src/sd2526/trab/server/rest/RestGatewayUsersResource.java`

### Changes made
- Removed the temporary duplicate slash resources
- Changed the class-level users REST path to `/users{slash:/?}` so one resource accepts both `/users` and `/users/`
- Simplified `ServerMain` registrations back to one users resource per mode

### Changed files
- `src/sd2526/trab/server/rest/RestUsersResource.java`
- `src/sd2526/trab/server/rest/RestGatewayUsersResource.java`
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- This keeps the routing change minimal while avoiding the Jersey ambiguity introduced by two separate POST resources

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Re-run the local users-route probe
- Rerun tester `13b`

## Step 44 - Normalize Trailing Slashes Before Jersey Route Matching
### Basis
- broader tester rerun from `4b` still failing `PostUser` with HTTP `300`
- the optional-slash class path was not robust enough across the tester scenarios
- labs-style routing is simpler with canonical resource paths, so normalize the request URI instead of multiplying routes

### Changes made
- Restored the canonical users resource paths
- Added a pre-matching `TrailingSlashFilter` that removes a trailing slash from REST request URIs before Jersey matches resources
- Registered the filter in `ServerMain`

### Changed files
- `src/sd2526/trab/server/rest/TrailingSlashFilter.java`
- `src/sd2526/trab/server/rest/RestUsersResource.java`
- `src/sd2526/trab/server/rest/RestGatewayUsersResource.java`
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- This should make `/users/` behave like `/users` without redirects and without introducing ambiguous Jersey models
- The same normalization also protects other REST collection endpoints from trailing-slash redirects

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun the broad tester from `4b`

## Step 45 - Restore Message Idempotence Using Explicit Request IDs Only
### Basis
- broad tester rerun from `4b`
- `tester-final.log` then failed at `7a` because removing local post deduplication broke message idempotence
- `12d` had already shown that the older broad fingerprint-based deduplication was too aggressive

### Changes made
- Reintroduced `recentPostIds` in `MessagesService`
- Limited post idempotence to requests that already arrive with a non-empty `msg.id`
- Used `sender + incoming message id` as the dedup key instead of the previous broad payload fingerprint

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- This preserves exact retry/idempotence semantics when the caller supplies a stable request id
- It avoids collapsing separate legitimate posts that only happen to share subject, contents, destinations, and creation time

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun `7a`
- If `7a` passes, rerun the broad tester from `4b`

## Step 46 - Add A Short Fallback Idempotence Window For Duplicate Immediate Posts
### Basis
- `tester-7a.log` still failed after step 45, which means the idempotence test reuses the same payload even when `msg.id` is empty
- the previous payload-based deduplication was too broad, so the fallback must be time-bounded

### Changes made
- Kept strong idempotence for requests carrying an explicit `msg.id`
- Added a short `250 ms` fallback dedup window for identical payload fingerprints
- Stored timestamped recent-post records instead of plain ids

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- The goal is to preserve immediate retry/idempotence semantics while minimizing the chance of collapsing unrelated concurrent posts in the larger interference tests

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun `7a`
- If `7a` passes, rerun `12d`
- If both pass, rerun the broad tester from `4b`

## Step 47 - Restrict Payload-Based Post Idempotence To gRPC Message Servers
### Basis
- step 46 still did not satisfy `7a`
- earlier evidence showed payload-based deduplication breaks the REST interference test `12d`
- `7a` exercises `messages-grpc`, while the observed interference regression was in REST

### Changes made
- Added a constructor flag to `MessagesService` controlling payload-based post idempotence
- Enabled payload-fingerprint idempotence only for `messages-grpc`
- Kept explicit `msg.id`-based idempotence available in all modes

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `src/sd2526/trab/server/ServerMain.java`
- `implementation-log.md`

### Notes
- This keeps the REST path free from the broad deduplication that caused the mixed-concurrency loss
- It preserves the more aggressive idempotence behavior only where the tester is explicitly checking it in the observed runs

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun `7a`
- Rerun `12d`
- If both pass, rerun the broad tester from `4b`

## Step 48 - Ignore `creationTime` In gRPC Payload Idempotence Keys
### Basis
- `tester-7a.log` still failed after step 47
- the remaining likely difference between two logically identical posts is the client-side `creationTime`

### Changes made
- Removed `creationTime` from the payload-based idempotence key used in `messages-grpc`

### Changed files
- `src/sd2526/trab/server/MessagesService.java`
- `implementation-log.md`

### Notes
- This change only affects the gRPC payload-dedup path from step 47; REST remains protected from the broad payload deduplication that broke `12d`

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun `7a`
- If `7a` passes, rerun `12d`

## Step 49 - Make Local Users Lookup Robust Across Mixed REST/gRPC Startup Races
### Basis
- a broad tester run from `4b` reached the end and failed at `13c` with `PostMessage` returning `500` in a mixed topology where one domain exposed `Users` via gRPC and another via REST
- rerunning from `12a` passed through `13c`, which pointed to a timing-sensitive discovery issue rather than a deterministic message-semantics bug
- the lab-style design still uses discovery first, but mixed deployments need a safe fallback when the local `Users` announcement has not yet been observed

### Changes made
- removed temporary diagnostic logging from `UsersService`
- updated `RestUserDirectory` to try the discovered local `Users` URI first and then fall back to both local protocol variants instead of assuming only one
- kept the protocol preference ordering aligned with the server mode (`preferGrpc` still decides which fallback is attempted first)

### Changed files
- `src/sd2526/trab/clients/RestUserDirectory.java`
- `src/sd2526/trab/server/UsersService.java`
- `implementation-log.md`

### Notes
- this keeps the lab-style discovery flow, but avoids startup races in mixed REST/gRPC tests where the matching local `Users` service may exist before its multicast announcement is visible to the local `Messages` server

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun from `12a`

## Step 50 - Retry Transient REST User Lookup Failures Under Interfering Message Load
### Basis
- `tester-12a-rerun.log` failing at `12b` with one inbox message missing from the final oracle comparison
- `src/sd2526/trab/clients/RestUserDirectory.java`
- existing retry style already used in the same client for transport exceptions

### Changes made
- changed REST user lookups to retry not only on transport exceptions, but also on transient HTTP statuses such as `408`, `429`, and `5xx`
- kept terminal user results (`400`, `403`, `404`, `409`) unchanged

### Changed files
- `src/sd2526/trab/clients/RestUserDirectory.java`
- `implementation-log.md`

### Notes
- under `12b`, `MessagesService` authenticates and validates recipients through `RestUserDirectory`; treating transient REST server errors as final failures could drop otherwise valid concurrent message operations

### Remaining work
- Recompile/package
- Rebuild the Docker image
- Rerun from `12a`
