const ROWS = 6;
const COLS = 7;
let currentPlayer = 1;
let board = [];

function createBoard() {
    let boardElement = document.getElementById('board');
    board = [];
    boardElement.innerHTML = '';

    for (let row = 0; row < ROWS; row++) {
        let rowElement = document.createElement('div');
        rowElement.classList.add('row');
        board.push([]);
        for (let col = 0; col < COLS; col++) {
            let cellElement = document.createElement('div');
            cellElement.classList.add('cell');
            cellElement.dataset.column = col;
            cellElement.addEventListener('click', () => dropPiece(col));
            rowElement.appendChild(cellElement);
            board[row].push(0);
        }
        boardElement.appendChild(rowElement);
    }
}

function dropPiece(col) {
    for (let row = ROWS - 1; row >= 0; row--) {
        if (board[row][col] === 0) {
            board[row][col] = currentPlayer;
            renderBoard();
            if (checkWin(row, col)) {
                setTimeout(() => alert(`Joueur ${currentPlayer} a gagné !`), 100);
                return;
            }
            currentPlayer = currentPlayer === 1 ? 2 : 1;
            return;
        }
    }
}

function checkWin(row, col) {
    function checkDirection(dRow, dCol) {
        let count = 1;
        let r, c;

        // Check forward
        r = row + dRow;
        c = col + dCol;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] === currentPlayer) {
            count++;
            r += dRow;
            c += dCol;
        }

        // Check backward
        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] === currentPlayer) {
            count++;
            r -= dRow;
            c -= dCol;
        }

        return count >= 4;
    }

    return (
        checkDirection(0, 1) || // Horizontal
        checkDirection(1, 0) || // Vertical
        checkDirection(1, 1) || // Diagonal /
        checkDirection(1, -1)   // Diagonal \
    );
}

function renderBoard() {
    let cells = document.querySelectorAll('.cell');
    cells.forEach((cell, index) => {
        let row = Math.floor(index / COLS);
        let col = index % COLS;
        if (board[row][col] === 0) {
            cell.style.backgroundColor = '#fff';
        } else if (board[row][col] === 1) {
            cell.style.backgroundColor = '#FF6347'; // Red
        } else {
            cell.style.backgroundColor = '#FFFF00'; // Yellow
        }
    });
}

function resetBoard() {
    currentPlayer = 1;
    createBoard();
    renderBoard();
}
