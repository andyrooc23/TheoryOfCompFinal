from typing import Set

from StateNode import StateNode as sn

edges = []
states = []
userInput = input()
with open(str(userInput)) as file:
    firstLine = file.readline().split()
    for i in range(1, len(firstLine)):
        edges.append(firstLine[i])
    # populate the map
    line = file.readline()
    while line:
        arr = line.split()
        state = arr[0]
        thisNode = sn(state)
        for i in range(1, len(arr)):
            thisNode.children[edges[i - 1]] = arr[i]
        states.append(thisNode)
        line = file.readline()

grid = []
marked = []
for a in states:
    x = a.val
    for b in states:
        y = b.val
        if x is not y and {x, y} not in grid:
            grid.append({x, y})
            if ('F' in x and 'F' not in y) or ('F' not in x and 'F' in y):
                marked.append({x, y})

# grid populated and first round of marking is done

for i in range(len(grid) - len(marked)):
    for combo in reversed(grid):
        if combo not in marked and len(combo) > 0:
            temp = combo.copy()
            oldY = temp.pop()
            oldX = temp.pop()
            for edge in edges:
                newY = states[int(oldY[0])].children[edge]
                newX = states[int(oldX[0])].children[edge]

                if {newX, newY} in marked:
                    marked.append(combo)

# finding all non marked states
# pop all of the unnecessary states
for x in marked:
    grid.remove(x)
# combine all left

# remove only the repeats
toDelete = dict()
for combo in grid:
    temp = combo.copy()
    second = temp.pop()
    first = temp.pop()
    i = int(second[0])
    j = int(first[0])

    # gross? combining all of the key pairs into a single map that maps all of the to delete ones to a single state to
    # keep and delete the rest
    if j not in toDelete.keys() and i not in toDelete.keys():
        if j not in toDelete.values() and i not in toDelete.values():
            toDelete[i] = set()
            toDelete[i].add(j)
        elif j in toDelete.values():
            for a in toDelete.keys():
                if j in toDelete[a]:
                    toDelete[a].add(i)
        elif i in toDelete.values():
            for a in toDelete.keys():
                if i in toDelete[a]:
                    toDelete[a].add(j)
    if i in toDelete.keys() and j in toDelete.keys():
        toDelete[i].add(x for x in toDelete[j])
        toDelete[i].add(j)
        toDelete.pop(j)
    elif i in toDelete.keys():
        toDelete[i].add(j)
    else:
        toDelete[j].add(i)


deleteStates = set()
for x in toDelete.keys():
    for y in toDelete[x]:
        if isinstance(y, int):
            deleteStates.add(y)
deleteStates = list(deleteStates)
deleteStates.sort()
for i in reversed(deleteStates):
    for edge in edges:
        stc = states[i].children[edge]
        if int(stc[0]) in deleteStates:
            for x in toDelete.keys():
                if i in toDelete[x]:
                    states[i].children[edge] = states[x].val
    del states[i]

print("             ", end='')
for x in edges:
    print(edge + "   ", end='')
print()
for x in states:
    print("state: " + x.val, end='')
    print(" " * (2 - len(x.val)) + " | ", end='')
    for y in x.children.values():
        print(y + " " * (4 - len(y)), end='')
    print()

print('Where: ')
for x in toDelete.keys():
    print(str(x) + " maps to ", end='')
    for y in toDelete[x]:
        if isinstance(y, int):
            print(str(y) + " : ", end='')
    print()


# regenerate DFA Table
