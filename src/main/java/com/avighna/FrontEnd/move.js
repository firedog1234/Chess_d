let socket = null;
function drawInitialBoard(chessBoard){
    const alph = ["A", "B", "C", "D", "E", "F", "G", "H"];
    const numbers = ["1", "2", "3", "4", "5", "6", "7", "8"];
    const board = document.getElementById("chessboard");
    board.innerHTML = "";


    for(let i = 0; i < 8; i++){
        for(let j = 0; j < 8; j++){
            const square = document.createElement("div");
            const letter = document.createElement("div");
            const number = document.createElement("div");
            const piece = document.createElement("div");


            square.setAttribute("tabindex", "0");

            letter.classList.add("letter");
            square.classList.add("square");
            number.classList.add("number");

            square.dataset.row = i;
            square.dataset.col = j;


            if(chessBoard[i][j] != ""){
                piece.classList.add("piece");
                piece.setAttribute("draggable", "true");
                piece.innerText = chessBoard[i][j];
                square.appendChild(piece);
            }

            if(i === 7){
                letter.innerText = alph[j];
                square.appendChild(letter);
            }

            if(j === 0){
                number.innerText = numbers[7 - i];
                square.appendChild(number);
            }

            if((i+j) % 2 === 0){
                square.classList.add("light");
            }
            else{
                square.classList.add("dark");
            }

            board.appendChild(square);

        }
    }
}

function movePieces(){
    const piece = document.querySelectorAll(".piece");
    const square = document.querySelectorAll(".square");
    let draggedPiece = null;

    piece.forEach(piece => {
        piece.addEventListener("dragstart", function(e){
            draggedPiece = e.target;
        });
    });

    square.forEach(square =>{
        square.addEventListener("dragover", function (e){
            e.preventDefault();
        });
        square.addEventListener("drop", function (e){

            e.preventDefault();

            if(draggedPiece === null || draggedPiece.innerText === "♟" || draggedPiece.innerText === "♜" || draggedPiece.innerText === "♞"
            || draggedPiece.innerText === "♝" || draggedPiece.innerText === "♛" || draggedPiece.innerText === "♚"){
                return;
            }

            const targetSquare = e.currentTarget;
            const targetRow = parseInt(targetSquare.dataset.row);
            const targetCol = parseInt(targetSquare.dataset.col);

            const sourceSquare = draggedPiece.parentElement;
            const sourceRow = parseInt(sourceSquare.dataset.row);
            const sourceCol = parseInt(sourceSquare.dataset.col);

            let theMoveData = {
              sourceRow,
              sourceCol,
              targetRow,
              targetCol
            };

            socket.send(JSON.stringify(theMoveData));

            draggedPiece = null;
        });
    });
}

function connectToWebSocket(){
    socket = new WebSocket("ws://localhost:8081");


    socket.onmessage = (event) => {
        try{
            const data = JSON.parse(event.data);
            console.log("data recieved from websocket", data);

            if(data.blacksMove === null){
                let fromRow = 8-data.rowFrom;
                let fromCol = data.colFrom;
                let toRow = 8-data.rowTo;
                let toCol = data.colTo;

                runBoard[toRow][toCol] = runBoard[fromRow][fromCol];
                runBoard[fromRow][fromCol] = "";

                drawInitialBoard(runBoard);
                movePieces();
            }

            if(data.status === "OK"){
                let sourceRow = data.sourceRow;
                let sourceCol = data.sourceColumn
                let targetRow = data.targetRow;
                let targetCol = data.targetColumn;

                runBoard[targetRow][targetCol] = runBoard[sourceRow][sourceCol];
                runBoard[sourceRow][sourceCol] = "";

                drawInitialBoard(runBoard);
                movePieces();
            } else{
                console.log("oh no");
            }
        } catch (e){
            console.log("Non-JSON message received:", event.data);
        }


    }

}



let runBoard = [
    ["♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜"],
    ["♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟"],
    ["",   "",   "",   "",   "",   "",   "",   ""  ],
    ["",   "",   "",   "",   "",   "",   "",   ""  ],
    ["",   "",   "",   "",   "",   "",   "",   ""  ],
    ["",   "",   "",   "",   "",   "",   "",   ""  ],
    ["♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙"],
    ["♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"]
];
connectToWebSocket()
drawInitialBoard(runBoard);
movePieces();
