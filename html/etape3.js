const words = ["PROGRAMMATION", "INFORMATIQUE", "DEVELOPPEMENT", "PYTHON", "JAVASCRIPT", "ALGORITHME"];
let randomWord = words[Math.floor(Math.random() * words.length)];
let hiddenWord = "_".repeat(randomWord.length);
let attemptsLeft = 6;
let guessedLetters = [];

const wordContainer = document.getElementById("word-container");
const message = document.getElementById("message");

function displayWord() {
    wordContainer.textContent = hiddenWord.split("").join(" ");
}

function guessLetter() {
    const input = document.getElementById("letter").value.toUpperCase();
    if (!input || !input.match(/[A-Z]/)) {
        message.textContent = "Veuillez entrer une lettre valide (A-Z).";
        return;
    }
    if (guessedLetters.includes(input)) {
        message.textContent = "Vous avez déjà essayé cette lettre.";
        return;
    }
    guessedLetters.push(input);
    if (randomWord.includes(input)) {
        message.textContent = "Bonne devinette !";
        revealLetters(input);
    } else {
        attemptsLeft--;
        message.textContent = "Mauvaise devinette. Il vous reste " + attemptsLeft + " essais.";
    }
    checkGameStatus();
    displayWord();
    document.getElementById("letter").value = "";
}

function revealLetters(letter) {
    for (let i = 0; i < randomWord.length; i++) {
        if (randomWord[i] === letter) {
            hiddenWord = hiddenWord.substring(0, i) + letter + hiddenWord.substring(i + 1);
        }
    }
}

function checkGameStatus() {
    if (hiddenWord === randomWord) {
        message.textContent = "Félicitations ! Vous avez trouvé le mot : " + randomWord + ".";
        disableInput();
    } else if (attemptsLeft === 0) {
        message.textContent = "Vous avez perdu. Le mot était : " + randomWord + ".";
        disableInput();
    }
}

function disableInput() {
    document.getElementById("letter").disabled = true;
    document.querySelector("button").disabled = true;
}
