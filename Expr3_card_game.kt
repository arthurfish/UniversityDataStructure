/*
小猫钓鱼纸牌游戏

二. 问题描述
A和B两位同学玩简单的纸牌游戏，每人手里都有n张牌，两人轮流出牌并依次排列在桌面上，每次出掉手里的第1张牌，出牌后如果发现桌面上有跟刚才打出的牌的数字相同的牌，则把从数字相同的那张牌开始的全部牌依次放在自己手里的牌的末尾。当一个人手里的牌先出完时，游戏结束，对方获胜。
如n为5，A手里的牌依次为2 3 5 6 1，B手里的牌依次为1 5 4 2 9；
A出2；
B出1；
A出3；
B出5；
A出5，发现前面有一张5，则把两个5都拿掉，这时他手里有6 1 5 5；
桌子上的牌依次为2 1 3；
B出4；
A出6；
B出2，发现前面有一张2，则把从2开始的牌全部拿掉，这时他手里有9 2 1 3 4 6 2；
桌子上没有牌了；
A出1；
B出9；
A出5；
B出2；
依次类推，直到某人先出完牌为止，则对方是胜者。
编写程序，利用栈和队列，判断谁是胜者。
 */

import java.util.*

class Player(val name: String){
    var cards: Deque<Int> = LinkedList()
    fun failure() = cards.isEmpty()
    override fun toString(): String = "$name: [${cards.joinToString()}]"
}

fun simulateLoser(): String {
    val A = Player("A")
    A.cards.addAll(listOf(2, 3, 5, 6, 1))
    val B = Player("B")
    B.cards.addAll(listOf(1, 5, 4, 2, 9))
    val deck: Deque<Int> = LinkedList()
    for(i in 1..30){
        println("===ROUND BEGIN===")
        println("deck: [${deck.joinToString()}]")
        for(player in listOf(A, B)){
            println(player)
            if(player.failure()){
                return player.name
            }
            val playedCard = player.cards.pollFirst()
            println("${player.name} plays $playedCard")
            val equalCardPos = deck.indexOfFirst { it == playedCard }
            deck.addLast(playedCard)
            if (equalCardPos != -1){

                val reverseStack = LinkedList<Int>()
                val expectedCardTook = deck.size - equalCardPos
                for(j in 1..expectedCardTook){
                    reverseStack.push(deck.pollLast())
                }
                for(j in 1..expectedCardTook){
                    player.cards.offerLast(reverseStack.pop())
                }
                println("${player.name} got extra cards: [${player.cards.joinToString() }]")
            }
        }
        println("DECK: [${deck.joinToString()}]")
        println("===ROUND END===")
    }
    throw AssertionError()
}

fun main() {
    println("The loser is: ${simulateLoser()}")
}