package com.gvt.chessboard;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.chessboard.Piece.Color;

public class Chessboard {

	private static Logger logger = LoggerFactory.getLogger(Chessboard.class);

	private static final String[] algebraicAnnotationForCols = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
	private static final String[] algebraicAnnotationForRows = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };

	private boolean whitePiecesBottom = false;
	private Square[][] squares = new Square[8][8];

	public Chessboard(boolean whitePiecesBottom) {
		this.whitePiecesBottom = whitePiecesBottom;

		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				squares[x][y] = new Square(convertCoordinatesToAlgebraic(x, y));
			}
		}
	}

	public void setPiece(String fenPiece, int coordinateX, int coordinateY) {
		Piece piece = null;

		switch (fenPiece) {
		case "p":
			piece = new Pawn(Color.BLACK);
			break;
		case "P":
			piece = new Pawn(Color.WHITE);
			break;
		case "r":
			piece = new Rook(Color.BLACK);
			break;
		case "R":
			piece = new Rook(Color.WHITE);
			break;
		case "n":
			piece = new Knight(Color.BLACK);
			break;
		case "N":
			piece = new Knight(Color.WHITE);
			break;
		case "b":
			piece = new Bishop(Color.BLACK);
			break;
		case "B":
			piece = new Bishop(Color.WHITE);
			break;
		case "q":
			piece = new Queen(Color.BLACK);
			break;
		case "Q":
			piece = new Queen(Color.WHITE);
			break;
		case "k":
			piece = new King(Color.BLACK);
			break;
		case "K":
			piece = new King(Color.WHITE);
			break;
		default:
			break;
		}

		Square square = new Square(convertCoordinatesToAlgebraic(coordinateX, coordinateY), piece);

		squares[coordinateX][coordinateY] = square;
	}

	public void print(boolean onlyCoordinates) {
		logger.debug("************ Chessboard ***************");
		logger.debug("");

		StringBuilder row = new StringBuilder();

		if (whitePiecesBottom) {
			row.append("\ta\tb\tc\td\te\tf\tg\th");
			logger.debug("{}", row);

			for (int y = 0; y < 8; ++y) {
				row = new StringBuilder();
				row.append(8 - y);
				for (int x = 0; x < 8; ++x) {
					if (onlyCoordinates) {
						row.append("\t" + squares[x][y].getAlgebraicCoordinate());
					} else {
						row.append("\t" + squares[x][y]);
					}
				}

				logger.debug("{}", row);
			}

			row = new StringBuilder();
			row.append("\ta\tb\tc\td\te\tf\tg\th");
			logger.debug("{}", row);
		} else {
			row.append("\th\tg\tf\te\td\tc\tb\ta");
			logger.debug("{}", row);

			for (int y = 0; y < 8; ++y) {
				row = new StringBuilder();
				row.append(y + 1);
				for (int x = 0; x < 8; ++x) {
					if (onlyCoordinates) {
						row.append("\t" + squares[x][y].getAlgebraicCoordinate());
					} else {
						row.append("\t" + squares[x][y]);
					}
				}

				logger.debug("{}", row);
			}

			row = new StringBuilder();
			row.append("\th\tg\tf\te\td\tc\tb\ta");
			logger.debug("{}", row);
		}
	}

	public String compare(Chessboard previousChessboard) {
		logger.debug("Starting to compare");

		Square startingSquare;
		Square finalSquare = null;

		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				if (previousChessboard.squares[x][y].toString().equals(squares[x][y].toString())) {
					logger.debug("Equal squares previous:{} current:{}", previousChessboard.squares[x][y], squares[x][y]);
				} else {
					logger.debug("Not equals previous:{} current:{}", previousChessboard.squares[x][y], squares[x][y]);

					if (previousChessboard.squares[x][y].toString().equals("X")) {
						logger.debug("The previous square is empty, starting point?");
						startingSquare = previousChessboard.squares[x][y];

						logger.debug("The current square is full, final point?");
						finalSquare = squares[x][y];
					}

					logger.debug("potencial play:{}", finalSquare.getPiece().getFenLetter() + "" + finalSquare.getAlgebraicCoordinate());
				}
			}
		}

		return null;
	}

	public Square getSquare(int coordinateX, int coordinateY) {
		return squares[coordinateX][coordinateY];
	}

	public Square getSquare(String algebraicCoordinate) {
		String col = algebraicCoordinate.substring(0, 1);
		int row = Integer.parseInt(algebraicCoordinate.substring(1, 2));

		logger.trace("Col:{} row:{}", col, row);

		int colInArray = ArrayUtils.indexOf(algebraicAnnotationForCols, col);

		if (whitePiecesBottom) {
			return squares[colInArray][Math.abs(row - 8)];
		}

		return squares[Math.abs(colInArray - 7)][row - 1];
	}

	private String convertCoordinatesToAlgebraic(int coordinateX, int coordinateY) {
		String retValue = null;

		if (whitePiecesBottom) {
			retValue = algebraicAnnotationForCols[coordinateX] + algebraicAnnotationForRows[Math.abs(coordinateY - 7)];
		} else {
			retValue = algebraicAnnotationForCols[Math.abs(coordinateX - 7)] + algebraicAnnotationForRows[coordinateY];
		}

		return retValue;
	}

}
