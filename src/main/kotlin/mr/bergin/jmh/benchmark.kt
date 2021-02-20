package mr.bergin.jmh

import arrow.core.Either
import reactor.core.publisher.Mono


class NotFilledInException : RuntimeException("", null, true, false)
class FilledInException : RuntimeException()

fun <K> benchmark(
    stackDepth: UInt = 0.toUInt(),
    calculation: () -> K,
): K {
    fun simulatedStackDepth(depthRemaining: UInt): K {
        return if (depthRemaining == 0.toUInt()) {
            calculation()
        } else {
            simulatedStackDepth(depthRemaining - 1.toUInt())
        }
    }
    return simulatedStackDepth(stackDepth)
}

fun handleExceptionInMono(throwsTest: () -> Nothing) = Mono.fromSupplier(throwsTest)
    .onErrorResume { Mono.empty() }
    .subscribe()

fun handleEitherInMono(eitherTest: () -> Either<Throwable, *>) = Mono.fromSupplier(eitherTest)
    .flatMap { it.fold({ t -> Mono.error(t) }, { r -> Mono.just(r) }) }
    .onErrorResume { Mono.empty() }
    .subscribe()