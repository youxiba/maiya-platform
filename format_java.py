#!/usr/bin/env python3
"""Java source code formatter - fixes braces, indentation, spacing."""
import os, re, glob

BASE = r"d:/workspace/java/maiya/maiya-platform"

def format_java(text):
    """Format Java source code with proper indentation and line breaks."""
    lines = text.replace('\r\n', '\n').split('\n')
    result = []
    indent = 0
    in_block_comment = False
    in_annotation = False

    for raw_line in lines:
        line = raw_line.rstrip()

        # Track block comments
        if '/*' in line and '*/' not in line:
            in_block_comment = True
        if '*/' in line:
            in_block_comment = False
            result.append(line)
            continue

        if in_block_comment:
            result.append(line)
            continue

        stripped = line.strip()

        # Skip empty lines
        if not stripped:
            result.append('')
            continue

        # Handle annotations
        if stripped.startswith('@'):
            in_annotation = True
            result.append(line)
            continue

        if in_annotation:
            in_annotation = False
            result.append(line)
            continue

        # Decrease indent for closing braces
        if stripped.startswith('}'):
            indent = max(0, indent - 1)

        # Apply indentation
        indent_str = '    ' * indent
        formatted = indent_str + stripped

        # Handle package/import declarations - keep as single lines
        if stripped.startswith(('package ', 'import ', 'package', 'import')):
            result.append(stripped)
            continue

        # Handle braces on end of line (Java style)
        if stripped.endswith('{') or stripped.endswith('{'):
            result.append(formatted)
            indent += 1
        elif stripped.endswith('};'):
            # End of enum with semicolon
            result.append(formatted)
        elif stripped.startswith('@'):
            result.append(formatted)
        elif stripped.startswith(('public ', 'private ', 'protected ', 'static ', 'final ', 'abstract ', 'class ', 'interface ', 'enum ', '@interface')):
            result.append(formatted)
        else:
            result.append(formatted)

    return '\n'.join(result)


def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    formatted = format_java(content)
    if formatted != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(formatted)
        return True
    return False


def main():
    count = 0
    for root, dirs, files in os.walk(BASE):
        if 'target' in root or 'node_modules' in root:
            continue
        for f in files:
            if f.endswith('.java'):
                path = os.path.join(root, f)
                if process_file(path):
                    count += 1
                    if count <= 10:
                        print(f"  {path[len(BASE)+1:]}")
    print(f"\\nFormatted {count} files")

if __name__ == '__main__':
    main()
