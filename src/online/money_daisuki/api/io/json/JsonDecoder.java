package online.money_daisuki.api.io.json;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;

import online.money_daisuki.api.io.PushbackReader;

public final class JsonDecoder {
	private final PushbackReader in;
	
	public JsonDecoder(final Reader in) {
		if(in == null) {
			throw new IllegalArgumentException("in == null");
		}
		this.in = new PushbackReader(new BufferedReader(in));
	}
	
	public JsonElement decode() throws IOException {
		return(readDatatype());
	}
	
	private JsonElement readDatatype() throws IOException {
		final int first = readNextToken();
		return(readDatatypeFor(first));
	}
	private JsonElement readDatatypeFor(final int c) throws IOException {
		if(c == -1) {
			throw new RuntimeException(new EOFException());
		}
		
		switch((char)c) {
			case('{'):
				return(parseMapContent());
			
			case('['):
				return(parseListContent());
			
			case('n'):
				final char u = (char) in.read();
				final char l0 = (char) in.read();
				final char l1 = (char) in.read();
				
				if(u == 'u' && l0 == 'l' && l1 == 'l') {
					return(new JsonNullDataElement());
				}
			case('\"'):
				return(parseStringContent());
			
			case('t'):
				char d;
				if((d = (char) in.read()) != 'r') {
					throw createUnexpectedContentException(d);
				} else if((d = (char) in.read()) != 'u') {
					throw createUnexpectedContentException(d);
				} else if((d = (char) in.read()) != 'e') {
					throw createUnexpectedContentException(d);
				}
				return(new JsonBoolDataElement(true));
				
			case('f'):
				if((d = (char) in.read()) != 'a') {
					throw createUnexpectedContentException(d);
				} else if((d = (char) in.read()) != 'l') {
					throw createUnexpectedContentException(d);
				} else if((d = (char) in.read()) != 's') {
					throw createUnexpectedContentException(d);
				} else if((d = (char) in.read()) != 'e') {
					throw createUnexpectedContentException(d);
				}
				return(new JsonBoolDataElement(false));
				
			case('+'):
			case('-'):
			case('0'):
			case('1'):
			case('2'):
			case('3'):
			case('4'):
			case('5'):
			case('6'):
			case('7'):
			case('8'):
			case('9'):
			case('.'):
				in.pushBack(c);
				return(parseNumber());
		}
		throw createUnexpectedContentException((char)c);
	}
	private RuntimeException createUnexpectedContentException(final char c) {
		return(new RuntimeException("Could not parse, unexcepted content: " + c));
	}
	
	private JsonElement parseStringContent() throws IOException {
		//Parse all after the first "
		final StringBuilder s = new StringBuilder();

		boolean inEscaping = false;

		int i;
		do {
			i = in.read();
			if(i == -1) {
				throw new RuntimeException(new EOFException());
			}
			if(inEscaping) {
				switch((char)i) {
					case('n'):
						i = '\n';
					break;
					case('r'):
						i = '\r';
					break;
					case('t'):
						i = '\t';
					break;
					case('f'):
						i = '\f';
					break;

					case('"'):
						// To avoid leaving the loop on a escaped quote
						inEscaping = false;
						s.append((char)i);
						continue;

					case('\\'):
					case('/'):
						break;

					default:
						throw new RuntimeException("Unsupported escape sequence: \\" + (char)i);
				}
				inEscaping = false;

			} else if(i == '\\') {
				inEscaping = true;
				continue;
			}

			if(i != '\"') {
				s.append((char)i);
			} else {
				break;
			}
		} while(true);
		return(new JsonStringDataElement(s.toString()));
	}
	private JsonElement parseListContent() throws IOException {
		final MutableJsonList list = new DefaultJsonList();

		boolean first = true;
		for(int i, j; (i = readNextToken()) != ']';) {
			if(first) {
				j = i;
				first = false;
			} else if(i == ',') {
				j = readNextToken();

				if(j == ']') { // Not really nice, but element list may ends with a ,
					break;
				}
			} else {
				throw new RuntimeException("Could not parse, unexcepted content: " + (char)i);
			}
			list.add(readDatatypeFor(j));
		}
		return(list);
	}
	private JsonElement parseMapContent() throws IOException {
		final MutableJsonMap map = new DefaultJsonMap();

		int i = readNextToken();
		if(i == '}') {
			return(map);
		}

		for(;; i = readNextToken()) {
			if(i == -1) {
				throw new IOException("Connection reset");
			}

			if(i == '}') {
				return(map);
			}
			if(i != '"') {
				throw new RuntimeException("Could not parse, unexcepted content: \"" + (char)i + "\"");
			}

			final String key = parseStringContent().asData().asString();
			i = readNextToken();
			if(i != ':') {
				throw new RuntimeException("Could not parse, unexcepted content: \"" + (char)i + "\"");
			}

			map.put(key, readDatatype());

			i = readNextToken();
			if(i != ',') {
				if(i != '}') {
					throw new RuntimeException("Could not parse, unexcepted content in map: \"" + (char)i + "\", expecting coma or closing map brakes.");
				} else {
					in.pushBack(i);
				}
			}
		}
	}
	
	private JsonElement parseNumber() throws IOException {
		boolean isFloat = false;

		final StringBuilder s = new StringBuilder();
		for(;;) {
			final int i = in.read();
			if((i < '0' || i > '9') && (i != '+' && i != '-' && i != '.' && i != 'E' && i != 'e')) {
				in.pushBack(i);
				break;
			} else if(i == '.' || i == 'e' || i == 'E') {
				isFloat = true;
			}
			s.append((char)i);
		}

		if(isFloat) {
			return(new JsonFloatDataElement(new BigDecimal(s.toString())));
		}
		return(new JsonIntDataElement(s.toString()));
	}
	
	private int readNextToken() throws IOException {
		int i;
		do {
			i = in.read();
			
			if(i == '/') {
				i = in.read();
				if(i == '/') {
					do {
						i = in.read();
					} while(i != '\r' && i != '\n' && i != -1);
				} else if(i == '*') {
					i = in.read();
					int j;
					for(;; i = j) {
						j = in.read();
						if((i == '*' && j == '/') || j == -1) {
							break;
						}
					}
					i = in.read();
				} else {
					in.pushBack(i);
					break;
				}
			}
		} while(i == ' ' || i == '\n' || i == '\r' || i == '\t');
		return(i);
	}
}
